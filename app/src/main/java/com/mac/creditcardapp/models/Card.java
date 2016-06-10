package com.mac.creditcardapp.models;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Card extends SugarRecord implements Serializable {
    private String cardNumber;
    private String CVV;
    private String expiryMonth;
    private String expiryYear;
    private String address;
    private String cardHolderName;
    private boolean deleted;
    public Card(){

    }
    public Card(Card c){
        this.cardNumber = c.getCardNumber();
        this.CVV = c.getCVV();
        this.expiryMonth = c.getExpiryMonth();
        this.expiryYear = c.getExpiryYear();
        this.address = c.getAddress();
        this.cardHolderName = c.getCardHolderName();
        this.deleted = false;
    }
    public Card(String cardNumber, String CVV, String expiryMonth, String expiryYear, String address, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.CVV = CVV;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.address = address;
        this.cardHolderName = cardHolderName;
        this.deleted = false;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        if (cardNumber != null ? !cardNumber.equals(card.cardNumber) : card.cardNumber != null)
            return false;
        if (CVV != null ? !CVV.equals(card.CVV) : card.CVV != null) return false;
        if (expiryMonth != null ? !expiryMonth.equals(card.expiryMonth) : card.expiryMonth != null)
            return false;
        if (expiryYear != null ? !expiryYear.equals(card.expiryYear) : card.expiryYear != null)
            return false;
        if (address != null ? !address.equals(card.address) : card.address != null) return false;
        return !(cardHolderName != null ? !cardHolderName.equals(card.cardHolderName) : card.cardHolderName != null);
    }

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result + (CVV != null ? CVV.hashCode() : 0);
        result = 31 * result + (expiryMonth != null ? expiryMonth.hashCode() : 0);
        result = 31 * result + (expiryYear != null ? expiryYear.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (cardHolderName != null ? cardHolderName.hashCode() : 0);
        return result;
    }
}
