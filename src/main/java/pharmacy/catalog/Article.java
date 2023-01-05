package pharmacy.catalog;

import java.time.LocalDate;

import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.springframework.lang.NonNull;

@Entity

public class Article extends Product {


	public static enum ArticleType {
		CATALOG, ORDER, READYORDERS, INPREPARATION, MIXTURE;
	}


	@Column(length = 1000)
	private String image, customerName, customerTel, orderDate, description, packSize;
	private ArticleType type;
	private int id;
	private boolean prescription;
	private Money purchasePrice;
	private LocalDate expiryDate;


	@SuppressWarnings({"unused", "deprecation"})
	private Article() {
	}

	
	public Article(String name,
				   int id,
				   String packSize,
				   LocalDate expiryDate,
				   String image,
				   Money retailPrice,
				   Money purchasePrice,
				   String description,
				   ArticleType type,
				   String customerName,
				   String customerTel,
				   boolean prescription,
				   String orderDate) {

		super(name, retailPrice);

		this.purchasePrice = purchasePrice;
		this.image = image;
		this.description = description;
		this.type = type;
		this.id = id;
		this.customerName = customerName;
		this.customerTel = customerTel;
		this.prescription = prescription;
		this.orderDate = orderDate;
		this.expiryDate = expiryDate;
		this.packSize = packSize;

	}

	//Getters ------------------------------------------------------------------

	public Money getPurchasePrice() {
		return purchasePrice;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public ArticleType getType() {
		return type;
	}

	public int getID() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public String getPackSize() {
		return packSize;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}


	//Setters -----------------------------------------------------------------

	public void setID(int id) {
		this.id = id;
	}


	public void setType(ArticleType type) {
		this.type = type;
	}


	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public boolean isPrescription() {
		return prescription;
	}

	public void setPrescription(boolean prescription) {
		this.prescription = prescription;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}
}