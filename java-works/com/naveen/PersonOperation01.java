package com.naveen;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.not;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;



// this class has all the working wode 

public class PersonOperation01 {
	public static void main(final String[] args) {
		MongoClient mongoClient;

		if (args.length == 0) {
			// connect to the local database server
			mongoClient = MongoClients.create();
		} else {
			mongoClient = MongoClients.create(args[0]); 
		}
		
		

        // create codec registry for POJOs
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        
        
        
        // get handle to "mydb" database
        MongoDatabase database = mongoClient.getDatabase("navdb").withCodecRegistry(pojoCodecRegistry);

        // get a handle to the "people" collection
        MongoCollection<Person> collection = database.getCollection("people", Person.class);

        
        
        
        // drop all the data in it
//        collection.drop();
//        insertOne(collection);
//        insertMany(collection);
//        retriveData(collection);
//        searchOneOnCity(collection);
//        findOneWithAge(collection);
//        findAllRecords(database);
//    	  updateOne(collection);
//        updateMany(collection);
//        replaceOneRecord(collection);
//        deleteOneRecord(collection); 
//        deleteMultipleRecords(collection);

        
        mongoClient.close(); 
        
        
        Document d= new Document(); 
        
	}

	private static void deleteMultipleRecords(MongoCollection<Person> collection) {
		DeleteResult deleteResult = collection.deleteMany(eq("address.city", "Bengaluru"));
        System.out.println(deleteResult.getDeletedCount());
	}

	private static void deleteOneRecord(MongoCollection<Person> collection) {
		DeleteResult deleteOne = collection.deleteOne(eq("address.city", "Wimborne"));
        System.out.println(deleteOne.getDeletedCount()>0?"Record Deleted":"Record Not Found");
	}

	// the difference between the update and replace is in update only some part of the document is 
	// update, here the entire document has to be sent
	private static void replaceOneRecord(MongoCollection<Person> collection) {
		Person newPerson = new Person("naveen", 35, new Address(101, "Cotton Pet", "Bengaluru", "456789")); 
        
        UpdateResult replaceOne = collection.replaceOne(eq("name", "Charles Babbage"), newPerson); 
        System.out.println(replaceOne.getModifiedCount()>0?"Record Modified ":"No Matching Found");
	}

	private static void findAllRecords(MongoDatabase database) {
		// find all with the loop with out deprication 
        List<Document> restaurants = database.getCollection("people").find().into(new ArrayList<Document>());

        for (Document restaurant : restaurants) {
            System.out.println(restaurant);
        }
	}

	private static void updateMany(MongoCollection<Person> collection) {
		// Update Many, this will check if the field in address {} data model is present if not 
        // it will create the field with null value 
        UpdateResult updateResult = collection.updateMany(not(eq("address.zip", null)), set("zip", null));
        System.out.println(updateResult.getModifiedCount());
	}

	private static void updateOne(MongoCollection<Person> collection) {
		// Update One
        UpdateResult updateOne = collection.updateOne(eq("name", "Ada Lovelace"), combine(set("age", 23), set("name", "Ada Lovelace")));
        
        System.out.println(updateOne.getMatchedCount()>0?"Record found and update" :"Not Found ");
	}

	private static void findOneWithAge(MongoCollection<Person> collection) {
		for (Person doc : collection.find(gt("age", 19))) {
            System.out.println(doc);
        }
	}

	private static void searchOneOnCity(MongoCollection<Person> collection) {
		// now use a query to get 1 document out
        Person person = collection.find(eq("address.city", "London")).first();
        System.out.println(person);
	}

	private static void insertOne(MongoCollection<Person> collection) {
		// make a document and insert it
        Person ada = new Person(null, "Ada Byron", 20, new Address( "St James Square", "London", "W1"));
        System.out.println("Original Person Model: " + ada);
        collection.insertOne(ada);


        // Person will now have an ObjectId
        System.out.println("Mutated Person Model: " + ada);
	}

	private static void insertMany(MongoCollection<Person> collection) {
		List<Person> people = Arrays.asList(
                new Person("Charles Babbage", 45, new Address("5 Devonshire Street", "London", "W11")),
                new Person("Alan Turing", 28, new Address("Bletchley Hall", "Bletchley Park", "MK12")),
                new Person("Timothy Berners-Lee", 61, new Address("Colehill", "Wimborne", null))
        );

        collection.insertMany(people);
        System.out.println("total # of people " + collection.countDocuments());
	}

	// forEach is deprecated here, look at 
	@SuppressWarnings({ "unused", "deprecation" })
	private static void retriveData(MongoCollection<Person> collection) {
		Block<Person> printBlock = new Block<Person>() {
            public void apply(final Person person) {
                System.out.println(person);
            }
        };

        collection.find().forEach(printBlock);
	}

}
