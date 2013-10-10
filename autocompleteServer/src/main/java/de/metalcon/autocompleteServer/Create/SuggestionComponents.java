package de.metalcon.autocompleteServer.Create;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import de.metalcon.autocompleteServer.AppendingObjectOutputStream;

/**
 * 
 * Contains the data extracted from the HTTP-request, which will be inserted
 * into the suggestTree and stored to disc.
 * 
 * @author Christian Schowalter
 * 
 */
public class SuggestionComponents implements Serializable {

	private static final long serialVersionUID = 7311975384159541028L;

	private String suggestString;
	private Integer weight;
	private String key;
	private String imageBase64;
	private String indexName;

	public String getSuggestString() {
		return this.suggestString;
	}

	public void setSuggestString(String suggestString) {
		this.suggestString = suggestString;
	}

	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getImageBase64() {
		return this.imageBase64;
	}

	public void setImageBase64(String image) {
		this.imageBase64 = image;
	}

	public String getIndexName() {
		return this.indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void saveToDisc(File createFile) {
		try {
			if (!createFile.exists()) {

				FileOutputStream fileOutputStream = new FileOutputStream(
						createFile, true);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						fileOutputStream);
				objectOutputStream.writeObject(this);
				objectOutputStream.flush();
				objectOutputStream.close();
				fileOutputStream.close();
			}

			else {
				FileOutputStream fileOutputStream = new FileOutputStream(
						createFile, true);
				AppendingObjectOutputStream appendingObjectOutputStream = new AppendingObjectOutputStream(
						fileOutputStream);
				appendingObjectOutputStream.writeObject(this);
				appendingObjectOutputStream.flush();
				appendingObjectOutputStream.close();
				fileOutputStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
