package com.bridegelabz.fundoo.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridegelabz.fundoo.notes.model.Notes;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("elasticSearch")
public class ElasticSearchImpl implements IElasticSearch
{
	private final String INDEX = "noteindex";
	private final String TYPE = "notetype";  

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public Notes create(Notes note) {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = objectMapper.convertValue(note, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX,TYPE,String.valueOf(note.getId())).source(dataMap);
		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return note;
	}
	
	public Notes updateNote(Notes note) {
		System.out.println("Update");
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getId()));
		@SuppressWarnings("unchecked")
		Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);
		updateRequest.doc(documentMapper);
		try {
			@SuppressWarnings("unused")
			UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
			System.out.println("Update SuccessFully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return note;
	}
	
	@SuppressWarnings("unused")
	public void deleteNote(int NoteId) {
		System.out.println("Delete");
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(NoteId));
		try {
			DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Notes> searchData(String query,int  userId){
		System.out.println("Query "+query);
		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
QueryBuilder queryBuilder = QueryBuilders.boolQuery()
		.must(QueryBuilders.queryStringQuery("*"+query+"*").analyzeWildcard(true).field("title", 2.0f)
		.field("description").field("labels"))
		
		.filter(QueryBuilders.termsQuery("user.id", String.valueOf(userId)));
System.out.println("QuryBuider="+queryBuilder);
//
//BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//queryBuilder.filter(QueryBuilders.multiMatchQuery(query, "content.query.*"));
//

searchSourceBuilder.query(queryBuilder);

searchRequest.source(searchSourceBuilder);

SearchResponse searchResponse=null;
try {
	searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

	System.out.println(searchResponse);
} catch (IOException e) {
	e.printStackTrace();
}

List<Notes> allnote=getSearchResult(searchResponse);


return allnote;





}


private List<Notes> getSearchResult(SearchResponse response) {

SearchHit[] searchHits = response.getHits().getHits();
List<Notes> notes = new ArrayList<>();
for (SearchHit hit : searchHits) {
	notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Notes.class));
}

System.out.println(notes);
return notes;
}



	//My
	
//	public List<Notes> searchData(String query, int userId) {
//		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//				.must(QueryBuilders.queryStringQuery("*" + query + "*")
//				.analyzeWildcard(true)
//				.field("title")
//				.field("description"))
//				.filter(QueryBuilders.termsQuery("userId", String.valueOf(userId)));
//		System.out.println();
//		searchSourceBuilder.query(queryBuilder);
//		searchRequest.source(searchSourceBuilder);
//		SearchResponse searchResponse = null;
//		try {
//			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//			System.out.println(searchResponse);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		List<Notes> allnote = getSearchResult(searchResponse);
//
//		return allnote;
//	}
//
//	private List<Notes> getSearchResult(SearchResponse response) {
//		SearchHit[] searchHits = response.getHits().getHits();
//		List<Notes> notes = new ArrayList<>();
//		for (SearchHit hit : searchHits) {
//			notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Notes.class));
//		} 
//		System.out.println(notes);
//		return notes;
//	}

}





//Ansar
//	private final String INDEX = "noteindex";
//	private final String TYPE = "notetype"; 
//	
//	@Autowired
//	private RestHighLevelClient client;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
////	@Override
//	public Notes crseate(Notes note) {
//		@SuppressWarnings("unchecked")
//		Map<String, Object> dataMap = objectMapper.convertValue(note, Map.class);
//		IndexRequest indexRequest = new IndexRequest(INDEX,TYPE,String.valueOf(note.getId())).source(dataMap);
//		try {
//			client.index(indexRequest, RequestOptions.DEFAULT);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return note;
//	}
//
//	
//	public void create(Notes note) 
//	{	
//		// Converting object into map
//		@SuppressWarnings("rawtypes")
//		Map noteMapper = objectMapper.convertValue(note, Map.class);
//		IndexRequest indexRequest = new IndexRequest("fundoo_note", "note_info", noteMapper.get("id")+"").source(noteMapper);
//		try {
//			client.index(indexRequest, RequestOptions.DEFAULT);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public Notes updateNote(Notes note) {
//		System.out.println("Update");
//		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getId()));
//		@SuppressWarnings("unchecked")
//		Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);
//		updateRequest.doc(documentMapper);
//		try {
//			@SuppressWarnings("unused")
//			UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
//			System.out.println("Update SuccessFully");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return note;
//	}
//
//	@Override
//	public void deleteNote(int NoteId) {
//		System.out.println("Delete");
//		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(NoteId));
//		try {
//			DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//////	@Override
////	public List<Notes> searchData(String query, int userId) {
////		System.out.println("Query : "+query);
////		System.out.println("INDEX : "+INDEX);
////		System.out.println("TYPE : "+TYPE);
////		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
////		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//////		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//////				.must(QueryBuilders.queryStringQuery("*" +query+ "*").analyzeWildcard(true).field("title")
//////				.field("description"));
////				//.filter(QueryBuilders.termsQuery("userId", String.valueOf(userId)));
////		
////		
////		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(query).field("description"));
////		
////		System.out.println("hbgh"+queryBuilder);
////		
////		searchSourceBuilder.query(queryBuilder);
////		searchRequest.source(searchSourceBuilder);
////		
////		System.out.println("helllo work");
////		
//////		SearchResponse searchResponse = null;
//////		
//////		try {
//////			System.out.println("Search Request : "+searchRequest);
//////			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//////			System.out.println("response"+searchResponse);
//////		} catch (IOException e) {
//////
//////			e.printStackTrace();
//////		}
//////		System.out.println("response"+searchResponse);
//////
//////		List<Notes> allnote = getSearchResult(searchResponse);
//////		System.out.println("Elastic Search Data"+allnote);
//////
//////		return allnote;
////		
////		SearchResponse searchResponse = null;
////		System.out.println(searchRequest.toString());
////		try {
////			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////		
////		ArrayList<Notes> listOfSearchedNotes = new ArrayList<>();
////		
////		searchResponse.getHits().spliterator().forEachRemaining(note->{
////			try {
////				listOfSearchedNotes.add(objectMapper.readValue(note.getSourceAsString(), Notes.class));
////			} catch (IOException e) {
////				e.printStackTrace();
////			}});
//////		searchSourceBuilder.query()
////		
////		return listOfSearchedNotes;
////	}
////	private List<Notes> getSearchResult(SearchResponse response) {
////		SearchHit[] searchHits = response.getHits().getHits();
////		List<Notes> notes = new ArrayList<>();
////		for (SearchHit hit : searchHits) {
////			notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Notes.class));
////		} 
////		System.out.println("notessss"+notes);
////		return notes;
////	}
//	
//public List<Notes> searchedNotes(String index, String type, Map<String, Float> fields, String searchText) {
//		
//	SearchRequest searchRequest = new SearchRequest("fundoo_note").types("note_info");
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		BoolQueryBuilder boolQuery = boolQuery(fields, searchText, 226);
//		searchSourceBuilder.query(boolQuery);
//		searchRequest.source(searchSourceBuilder);
//		
//		SearchResponse searchResponse = null;
//		System.out.println(searchRequest.toString());
//		try {
//			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		ArrayList<Notes> listOfSearchedNotes = new ArrayList<>();
//		
//		searchResponse.getHits().spliterator().forEachRemaining(note->{
//			try {
//				listOfSearchedNotes.add(objectMapper.readValue(note.getSourceAsString(), Notes.class));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}});
////		searchSourceBuilder.query()
//		
//		return listOfSearchedNotes;
//	}
//
//	// method to write search bool query
//	private BoolQueryBuilder boolQuery(Map<String, Float> fields,String textToSearch, long userId)
//	{
//		// concatenating wild-card to search text
//		if(!textToSearch.startsWith("*")) {
//			textToSearch = "*"+textToSearch;
//		}
//		
//		if(!textToSearch.endsWith("*")) {
//			textToSearch = textToSearch+"*";
//		}
//		
////		Optional<User> findById = userRespository.findById(1l);
////		System.out.println(findById.get().toString());
//		BoolQueryBuilder searchQuery = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(textToSearch).fields(fields));
////		BoolQueryBuilder searchQuery = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(textToSearch).fields(fields));
//		
//		return searchQuery;
//	}
//
//	@Override
//	public List<Notes> searchData(String query, int userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	

