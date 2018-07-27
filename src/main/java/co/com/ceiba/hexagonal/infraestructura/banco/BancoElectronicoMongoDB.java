package co.com.ceiba.hexagonal.infraestructura.banco;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BancoElectronicoMongoDB implements BancoElectronico {

	private static final String SET = "$set";
	private static final String MONTO = "funds";
	private static final String MONGO_PORT = "mongo-port";
	private static final String MONGO_HOST = "mongo-host";
	private static final String ID = "_id";
	private static final String DEFAULT_DB = "loteriaDB";
	private static final String DEFAULT_CUENTAS_COLLECTION = "cuentasbancarias";

	private MongoClient mongoClient;
	private MongoCollection<Document> coleccionCuentasBancarias;

	public BancoElectronicoMongoDB() {
		connect();
	}

	public void connect() {
		connect(DEFAULT_DB, DEFAULT_CUENTAS_COLLECTION);
	}

	public void connect(String dbName, String accountsCollectionName) {
		if (mongoClient != null) {
			mongoClient.close();
		}
		mongoClient = new MongoClient(System.getProperty(MONGO_HOST),Integer.parseInt(System.getProperty(MONGO_PORT)));
		MongoDatabase database = mongoClient.getDatabase(dbName);
		coleccionCuentasBancarias = database.getCollection(accountsCollectionName);
	}

	@Override
	public void realizarPago(String numeroCuentaBancaria, int monto) {
		Document search = new Document(ID, numeroCuentaBancaria);
		Document update = new Document(ID, numeroCuentaBancaria).append(MONTO, monto);
		coleccionCuentasBancarias.updateOne(search, new Document(SET, update), new UpdateOptions().upsert(true));
	}

	@Override
	public int obtenerTotalEnCuenta(String numeroCuentaBancaria) {
		Document search = new Document(ID, numeroCuentaBancaria);
		List<Document> results = coleccionCuentasBancarias.find(search).limit(1).into(new ArrayList<Document>());
		if (!results.isEmpty()) {
			return results.get(0).getInteger(MONTO);
		} else {
			return 0;
		}
	}

	@Override
	public boolean transferirFondos(int monto, String numeroCuentaBancariaOrigen, String numeroCuentaBancariaDestino) {
		int totalEnCuentaOrigen = obtenerTotalEnCuenta(numeroCuentaBancariaOrigen);
		if (totalEnCuentaOrigen < monto) {
			return false;
		} else {
			int totalEnCuentaDestino = obtenerTotalEnCuenta(numeroCuentaBancariaDestino);
			realizarPago(numeroCuentaBancariaOrigen, totalEnCuentaOrigen - monto);
			realizarPago(numeroCuentaBancariaDestino, totalEnCuentaDestino + monto);
			return true;
		}
	}
}
