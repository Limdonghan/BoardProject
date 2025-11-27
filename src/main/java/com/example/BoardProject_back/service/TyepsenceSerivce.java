package com.example.BoardProject_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TyepsenceSerivce {

    private final Client client;

    /// 컬렉션(스키마) 생성 - 앱 실행 시 한 번만 수행하거나 초기화 API로 관리
    public void createBoardCollection() throws Exception {
        /// 이미 존재하면 삭제 (초기화용, 주의!)
        try {
            client.collections("boards").delete();
        } catch (Exception e) {
            /// 없으면 패스
        }

        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name("boards");

        /// 검색할 필드 정의
        List<Field> fields = new ArrayList<>();
        fields.add(new Field().name("board_id").type(FieldTypes.STRING)); /// ID는 문자열 권장
        fields.add(new Field().name("title").type(FieldTypes.STRING));
        fields.add(new Field().name("content").type(FieldTypes.STRING));
        fields.add(new Field().name("created_at").type(FieldTypes.INT64)); /// 정렬용 timestamp

        collectionSchema.fields(fields);
        collectionSchema.defaultSortingField("created_at"); /// 기본 정렬 기준

        client.collections().create(collectionSchema);
        System.out.println("Collection created!");
    }

    /// 데이터 색인 (글 작성 시 호출)
    public void indexBoard(Long id, String title, String content) throws Exception {
        Map<String, Object> document = new HashMap<>();
        document.put("id", id.toString());          /// Typesense의 고유 ID 필드는 'id'입니다.
        document.put("board_id", id.toString());
        document.put("title", title);
        document.put("content", content);
        document.put("created_at", System.currentTimeMillis() / 1000);

        /// upsert: 있으면 수정, 없으면 생성
        client.collections("boards").documents().upsert(document);
    }

    /// 검색 (키워드로 조회)
    public SearchResult searchBoard(String keyword) throws Exception {

        SearchParameters searchParameters = new SearchParameters()
                .q(keyword)                 /// 검색어
                .queryBy("title, content")  /// 검색 필드
                .sortBy("created_at:desc"); /// 정렬 기준


        /// 결과는 JSON String으로 반환됩니다. (객체 매핑 필요 시 ObjectMapper 사용)
        return client.collections("boards").documents().search(searchParameters);
    }
}
