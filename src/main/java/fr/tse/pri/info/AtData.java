package fr.tse.pri.info;
public class AtData {
double value;
String data;

public AtData(double value, String data) {
	this.value = value;
	this.data = data;
}
public double getValue() {
	return value;
}
public void setValue(double value) {
	this.value = value;
}
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}
public String toString() {
	return "[AT =" + value + ", Text : "+ data+ "]";
}
}
