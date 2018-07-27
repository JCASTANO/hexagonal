package co.com.ceiba.hexagonal.infraestructura.repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.Jugador;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;

public class RepositorioBilleteMongoDB implements RepositorioBillete {

	private static final String INC = "$inc";
	private static final String NUMEROS = "numeros";
	private static final String NUMERO_CELULAR = "numerocelular";
	private static final String NUMERO_CUENTA_BANCARIA = "numerocuentabancaria";
	private static final String CORREO_ELECTRONICO = "correoelectronico";
	private static final String SEQ = "seq";
	private static final String BILLETE_ID = "billeteId";
	private static final String ID = "_id";
	private static final String MONGO_PORT = "mongo-port";
	private static final String MONGO_HOST = "mongo-host";
	private static final String DEFAULT_DB = "loteriaDB";
	private static final String DEFAULT_TICKETS_COLLECTION = "billetes";
	private static final String DEFAULT_COUNTERS_COLLECTION = "contadores";

	private MongoClient mongoClient;
	private MongoCollection<Document> coleccionBilletes;
	private MongoCollection<Document> coleccionContadores;

	public RepositorioBilleteMongoDB() {
		connect();
	}

	public void connect() {
		connect(DEFAULT_DB, DEFAULT_TICKETS_COLLECTION, DEFAULT_COUNTERS_COLLECTION);
	}


	public void connect(String dbName, String ticketsCollectionName, String countersCollectionName) {
		if (mongoClient != null) {
			mongoClient.close();
		}
		mongoClient = new MongoClient(System.getProperty(MONGO_HOST),Integer.parseInt(System.getProperty(MONGO_PORT)));
		MongoDatabase database = mongoClient.getDatabase(dbName);
		coleccionBilletes = database.getCollection(ticketsCollectionName);
		coleccionContadores = database.getCollection(countersCollectionName);
		if (coleccionContadores.count() <= 0) {
			initCounters();
		}
	}

	private void initCounters() {
		Document doc = new Document(ID, BILLETE_ID).append(SEQ, 1);
		coleccionContadores.insertOne(doc);
	}

	public int getNextId() {
		Document find = new Document(ID, BILLETE_ID);
		Document increase = new Document(SEQ, 1);
		Document update = new Document(INC, increase);
		Document result = coleccionContadores.findOneAndUpdate(find, update);
		return result.getInteger(SEQ);
	}

	@Override
	public Optional<Billete> buscarPorId(int id) {
		Document find = new Document(BILLETE_ID, id);
		List<Document> resultados = coleccionBilletes.find(find).limit(1).into(new ArrayList<Document>());
		if (!resultados.isEmpty()) {
			Billete billete = crearBillete(resultados.get(0));
			return Optional.of(billete);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Billete> guardar(Billete billete) {
		int billeteId = getNextId();
		Document doc = new Document(BILLETE_ID, billeteId);
		doc.put(CORREO_ELECTRONICO, billete.getJugador().getCorreoElectronico());
		doc.put(NUMERO_CUENTA_BANCARIA, billete.getJugador().getNumeroCuentaBancaria());
		doc.put(NUMERO_CELULAR, billete.getJugador().getNumeroCelular());
		doc.put(NUMEROS, billete.getNumerosLoteria().obtenerNumerosComoString());
		coleccionBilletes.insertOne(doc);
		return Optional.of(billete);
	}

	@Override
	public Map<Integer, Billete> buscarTodos() {
		Map<Integer, Billete> map = new HashMap<>();
		List<Document> docs = coleccionBilletes.find(new Document()).into(new ArrayList<Document>());
		for (Document doc : docs) {
			Billete billete = crearBillete(doc);
			map.put(billete.getId(), billete);
		}
		return map;
	}

	@Override
	public void borrarTodos() {
		coleccionBilletes.deleteMany(new Document());
	}

	private Billete crearBillete(Document doc) {
		Jugador jugador = new Jugador(doc.getString(CORREO_ELECTRONICO), doc.getString(NUMERO_CUENTA_BANCARIA),doc.getString(NUMERO_CELULAR));
		int[] numArray = Arrays.asList(doc.getString(NUMEROS).split(",")).stream().mapToInt(Integer::parseInt).toArray();
		Set<Integer> numeros = new HashSet<>();
		for (int num : numArray) {
			numeros.add(num);
		}
		NumerosLoteria numerosLoteria = NumerosLoteria.crear(numeros);
		return new Billete(doc.getInteger(BILLETE_ID), jugador, numerosLoteria);
	}
}
