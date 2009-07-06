package org.perfidix.Perclipse.viewTreeTestdaten;

public class Person {
	
	public String firstName = "John";
	public String lastName = "Doe";
	public int age = 37;
	public Person[] children = new Person[0];
	public Person parent = null;
	
	public Person(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public Person(String firstName, String lastName, int age, Person[] children) {
		this(firstName, lastName, age);
		this.children = children;
		for (int i = 0; i < children.length; i++) {
			children[i].parent = this;
		}
	}

	public static Person[] example() {
		return new Person[] {
			new Person("Dan", "Rubel", 38, new Person[] {
				new Person("Beth", "Rubel", 8),
				new Person("David", "Rubel", 3)}),
			new Person("Eric", "Clayberg", 39, new Person[] {
				new Person("Lauren", "Clayberg", 6),
				new Person("Lee", "Clayberg", 4)}),
			new Person("Mike", "Taylor", 52)
		};
	}
	
	public String toString() {
		return firstName + " " + lastName;
	}
}
