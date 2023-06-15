package br.com.ecommerce.ecommerce.model.dto;

import java.util.ArrayList;
import java.util.List;

public class LeadCampanhaGetResponse {

    private String name;
    private String email;
    private String dayOfCycle = "0";
    private String scoring;
    //private String ipAddress;

    private LeadCampanhaCadastroGetResponse campaign = new LeadCampanhaCadastroGetResponse();

    private List<String> tags = new ArrayList<>();

    private List<CustomFieldValuesGetResponse> customFieldValues = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDayOfCycle() {
        return dayOfCycle;
    }

    public void setDayOfCycle(String dayOfCycle) {
        this.dayOfCycle = dayOfCycle;
    }

    public String getScoring() {
        return scoring;
    }

    public void setScoring(String scoring) {
        this.scoring = scoring;
    }

	/*public String getIpAddress() {
		return ipAddress;
	}*/

	/*public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}*/

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LeadCampanhaCadastroGetResponse getCampaign() {
        return campaign;
    }

    public void setCampaign(LeadCampanhaCadastroGetResponse campaign) {
        this.campaign = campaign;
    }

    public List<CustomFieldValuesGetResponse> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(List<CustomFieldValuesGetResponse> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }
}
