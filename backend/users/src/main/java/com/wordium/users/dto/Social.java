package  com.wordium.users.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public class Social {
    private String website;
    private String twitter;
    private String github;
    private String linkedin;

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getTwitter() { return twitter; }
    public void setTwitter(String twitter) { this.twitter = twitter; }
    public String getGithub() { return github; }
    public void setGithub(String github) { this.github = github; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
}
