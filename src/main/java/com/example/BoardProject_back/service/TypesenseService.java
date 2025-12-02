package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.PostSearchResultDTO;
import com.example.BoardProject_back.entity.PostEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypesenseService {

    ///  Typesense 내에서 데이터를 저장할 컬렉션(RDBMS의 테이블)이름을 posts로 정의
    private static final String COLLECTION_NAME = "posts";

    private final Client client;

    @PostConstruct
    public void createCollectionIfMissing() {
        try {
            /// 'posts' 컬렉션 존재 확인
            ensureCollectionExists();
        } catch (Exception e) {
            log.error("Failed to initialize Typesense collection", e);
        }
    }

    /**
     * 데이터 인덱싱 저장 or 수정
     */
    public void indexPost(PostEntity post) {
        try {
            ensureCollectionExists();
            /// map -> JSON Serialization를 하기 가능한 형태로 변환
            Map<String, Object> document = new HashMap<>();
            document.put("id", String.valueOf(post.getId()));
            document.put("post_id", post.getId());
            document.put("title", post.getTitle());
            document.put("content", post.getContext());
            document.put("author", post.getUser().getNickName());
            document.put("category", post.getCategory().getCategory());
            document.put("totalComments", post.getCommentCount());
            document.put("totalLikes", post.getLikeCount()- post.getDisLikeCount());
            document.put("created_at",
                    post.getCreatedAt() != null
                            ? post.getCreatedAt().toEpochSecond(ZoneOffset.UTC)
                            : System.currentTimeMillis() / 1000);       /// 검색 시 별도 정렬 조건 없으면 최신순으로 정렬하도록 기본 설정

        /// 게시글을 작성하거나 수정했을 때, 해당 내용을 Typesense에 동기화 upsert(save + update)
            client.collections(COLLECTION_NAME).documents().upsert(document);
        } catch (Exception e) {
            log.warn("Failed to index post {} into Typesense", post.getId(), e);
        }
    }
    /**
     * 데이터 삭제
     */
    public void deletePost(int postId) {
        try {
            ensureCollectionExists();
            /// postID를 기준으로 삭제를 요청
            client.collections(COLLECTION_NAME).documents(String.valueOf(postId)).delete();
        } catch (Exception e) {
            log.warn("Failed to delete post {} from Typesense index", postId, e);
        }
    }

    /**
     * 데이터 검색
     */
    public List<PostSearchResultDTO> search(String keyword) {
        List<PostSearchResultDTO> results = new ArrayList<>();

        try {
            ensureCollectionExists();

            SearchParameters searchParameters = new SearchParameters()
                    .q(keyword)                                     /// 검색어 (Query)
                    .queryBy("title,content,author,category")       /// 검색할 필드 대상
                    .sortBy("created_at:desc");                     /// 정렬기준 (최신순)

            SearchResult searchResult = client.collections(COLLECTION_NAME).documents().search(searchParameters);

            /// 검색 엔진이 찾아낸 결과물 List == null 빈값 반환
            if (searchResult.getHits() == null) {
                return results;
            }
            /// 검색 엔진이 찾아낸 결과물 List를 for문 돌리며 DTO 변환(매핑)
            for (SearchResultHit hit : searchResult.getHits()) {
                Map<String, Object> document = hit.getDocument();
                results.add(new PostSearchResultDTO(
                        Integer.parseInt(
                        document.get("post_id").toString()),
                        document.getOrDefault("title", "").toString(),
                        document.getOrDefault("content", "").toString(),
                        document.getOrDefault("author", "").toString(),
                        document.getOrDefault("category", "").toString(),
                        (Integer) document.getOrDefault("totalComments",""),
                        (Integer) document.getOrDefault("totalLikes",""),
                        (LocalDateTime) document.getOrDefault("created_at","")
                ));
            }
        } catch (Exception e) {
            log.warn("Failed to search posts in Typesense", e);
        }

        return results;
    }

    private void ensureCollectionExists() throws Exception {
        try {
            ///  posts 컬렉션이 존재 하는지 확인.
            client.collections(COLLECTION_NAME).retrieve();
            return;
        } catch (Exception ignored) {
        }

        /// 존재하지 않다면 CollectionSchema정의 후 새로 만들기
        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name(COLLECTION_NAME);

        /// Schema 정의
        List<Field> fields = new ArrayList<>();
        fields.add(new Field().name("id").type(FieldTypes.STRING));                         /// Typesense 내부 고유 ID
        fields.add(new Field().name("post_id").type(FieldTypes.INT32));                     /// 실제 DB의 PK
        fields.add(new Field().name("title").type(FieldTypes.STRING));                      /// 검색 대상 '게시글 제목'
        fields.add(new Field().name("content").type(FieldTypes.STRING).optional(true));     /// 검색 대상 '게시글 본문'
        fields.add(new Field().name("author").type(FieldTypes.STRING).optional(true));      /// 검색 대상 or 필터링 '게시글 작성자'
        fields.add(new Field().name("category").type(FieldTypes.STRING).optional(true));    /// 검색 대상 or 필터링 '카테고리'
        fields.add(new Field().name("totalComments").type(FieldTypes.INT32).optional(true));/// 검색 조회 '총 댓글 수'
        fields.add(new Field().name("totalLikes").type(FieldTypes.INT32).optional(true));   /// 검색 조회 '총 좋아요 수'
        fields.add(new Field().name("created_at").type(FieldTypes.INT64));                  /// 정렬 '생성일자'

        collectionSchema.fields(fields);
        collectionSchema.defaultSortingField("created_at");

        client.collections().create(collectionSchema);
        log.info("Created Typesense collection '{}'", COLLECTION_NAME);
    }

    /// 게시글 리스트를 받아서 싹 다 넣는 메서드
    public void indexAllPosts(List<PostEntity> posts) {
        log.info("기존 데이터 동기화 시작: 총 {}개", posts.size());

        for (PostEntity post : posts) {
            /// 기존에 만들어둔 단건 등록 메서드 재활용!
            indexPost(post);
        }

        log.info("기존 데이터 동기화 완료!");
    }
}
