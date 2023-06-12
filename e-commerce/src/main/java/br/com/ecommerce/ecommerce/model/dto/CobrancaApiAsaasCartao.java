package br.com.ecommerce.ecommerce.model.dto;

public class CobrancaApiAsaasCartao {

    private String customer;
    private String billingType;
    private float value;
    private String dueDate;
    private String description;
    private String externalReference;
    private DiscountApiAsaas discount = new DiscountApiAsaas();
    private FineApiAsaas fine = new FineApiAsaas();
    private InterestApiAsaas interest = new InterestApiAsaas();
    private boolean postalService = false;
    private float installmentValue;
    private Integer installmentCount;

    private CartaoCreditoApiAsaas creditCard = new CartaoCreditoApiAsaas();
    private HolderInfoCreditCardAsaas creditCardHolderInfo = new HolderInfoCreditCardAsaas();

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public DiscountApiAsaas getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountApiAsaas discount) {
        this.discount = discount;
    }

    public FineApiAsaas getFine() {
        return fine;
    }

    public void setFine(FineApiAsaas fine) {
        this.fine = fine;
    }

    public InterestApiAsaas getInterest() {
        return interest;
    }

    public void setInterest(InterestApiAsaas interest) {
        this.interest = interest;
    }

    public boolean isPostalService() {
        return postalService;
    }

    public void setPostalService(boolean postalService) {
        this.postalService = postalService;
    }

    public float getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(float installmentValue) {
        this.installmentValue = installmentValue;
    }

    public Integer getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }

    public CartaoCreditoApiAsaas getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CartaoCreditoApiAsaas creditCard) {
        this.creditCard = creditCard;
    }

    public HolderInfoCreditCardAsaas getCreditCardHolderInfo() {
        return creditCardHolderInfo;
    }

    public void setCreditCardHolderInfo(HolderInfoCreditCardAsaas creditCardHolderInfo) {
        this.creditCardHolderInfo = creditCardHolderInfo;
    }
}
