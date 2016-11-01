package com.tay.rest.controller;

public class TestUUID {
	public static void main(String[] args) throws InterruptedException {
		while(true) {
			System.out.println(java.util.UUID.randomUUID().toString());
			Thread.sleep(2000);
		}
	}
}
