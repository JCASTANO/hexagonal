package co.com.ceiba.hexagonal.infraestructura.eventolog;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;

public class EventoLogMongoDB implements EventoLog {

	private static final String MENSAJE = "mensaje";
	private static final String DEFAULT_DB = "loteriaDB";
	private static final String DEFAULT_EVENTS_COLLECTION = "eventos";

	private MongoClient mongoClient;
	private MongoCollection<Document> eventsCollection;

	public EventoLogMongoDB() {
		connect();
	}

	public void connect() {
		connect(DEFAULT_DB, DEFAULT_EVENTS_COLLECTION);
	}

	public void connect(String dbName, String eventsCollectionName) {
		if (mongoClient != null) {
			mongoClient.close();
		}
		mongoClient = new MongoClient(System.getProperty("mongo-host"),Integer.parseInt(System.getProperty("mongo-port")));
		MongoDatabase database = mongoClient.getDatabase(dbName);
		eventsCollection = database.getCollection(eventsCollectionName);
	}
	
	@Override
	public void printAll(List<String> eventos) {
		for (String evento : eventos) {
			Document document = new Document();
			document.put(MENSAJE,evento);
			eventsCollection.insertOne(document);
		}
	}
}
