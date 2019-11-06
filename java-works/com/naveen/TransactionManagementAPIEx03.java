package com.naveen;

import org.bson.Document;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.TransactionBody;

// https://docs.mongodb.com/manual/core/transactions/index.html
public class TransactionManagementAPIEx03 {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		final MongoClient client = MongoClients.create();

		client.getDatabase("mydb1").getCollection("foo").withWriteConcern(WriteConcern.MAJORITY)
				.insertOne(new Document("abc", 0));
		client.getDatabase("mydb2").getCollection("bar").withWriteConcern(WriteConcern.MAJORITY)
				.insertOne(new Document("xyz", 0));

		/* Step 1: Start a client session. */

		final ClientSession clientSession = client.startSession();

		TransactionOptions txnOptions = TransactionOptions.builder().readPreference(ReadPreference.primary())
				.readConcern(ReadConcern.LOCAL).writeConcern(WriteConcern.MAJORITY).build();

		TransactionBody txnBody = new TransactionBody<String>() {
			public String execute() {
				MongoCollection<Document> coll1 = client.getDatabase("mydb1").getCollection("foo");
				MongoCollection<Document> coll2 = client.getDatabase("mydb2").getCollection("bar");

				/*
				 * Important:: You must pass the session to the operations.
				 */
				coll1.insertOne(clientSession, new Document("_id", 1));
				coll2.insertOne(clientSession, new Document("xyz", 999));
				return "Inserted into collections in different databases";
			}
		};
		try {
			/*
			 * Step 4: Use .withTransaction() to start a transaction, execute the callback,
			 * and commit (or abort on error).
			 */

			clientSession.withTransaction(txnBody, txnOptions);
		} catch (RuntimeException e) {
			// some error handling
			System.out.println(e);
		} finally {
			clientSession.close();
		}

		System.out.println("done");
	}
}
