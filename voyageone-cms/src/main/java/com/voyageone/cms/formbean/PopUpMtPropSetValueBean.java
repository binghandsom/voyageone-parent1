package com.voyageone.cms.formbean;

import java.util.List;
import java.util.Map;

public class PopUpMtPropSetValueBean {
	
	private String channelId;
	
	private String txtValue;
	
	private List<Word> words;
	
	private int wordType;
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTxtValue() {
		return txtValue;
	}

	public void setTxtValue(String txtValue) {
		this.txtValue = txtValue;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public int getWordType() {
		return wordType;
	}

	public void setWordType(int wordType) {
		this.wordType = wordType;
	}
	
	public static class Word{
		private int wordType;
		private String wordValue;
		private List<Map<String, String>> complexValues;
		public int getWordType() {
			return wordType;
		}
		public void setWordType(int wordType) {
			this.wordType = wordType;
		}
		public String getWordValue() {
			return wordValue;
		}
		public void setWordValue(String wordValue) {
			this.wordValue = wordValue;
		}
		public List<Map<String, String>> getComplexValues() {
			return complexValues;
		}
		public void setComplexValues(List<Map<String, String>> complexValues) {
			this.complexValues = complexValues;
		}
		
	}
	
}
