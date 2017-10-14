package pe.edu.unmsm.fisi.upg.ads.dirtycode.domain;

import java.util.Arrays;
import java.util.List;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	//private int exp;
	private int experience;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;

	public Integer register(IRepository repository) throws Exception {
		Integer speakerId = null;
		boolean good = false;
		boolean appr = false;
		//String[] nt = new String[] { "Microservices", "Node.js", "CouchDB", "KendoUI", "Dapper", "Angular2" };
		//String[] ot = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };
		String[] myArray = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };
		
		//DEFECT #5274 DA 12/10/2012
		//We weren't filtering out the prodigy domain so I added it.
		List<String> domains = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "compuserve.com");
		
		if (!this.firstName.isEmpty()) {
			if (!this.lastName.isEmpty()) {
				if (!this.email.isEmpty()) {
					//put list of employers in array
					//List<String> emps = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals", "Telerik");
					List<String> employees = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals", "Telerik");
					//good = ((this.exp > 10 || this.hasBlog || this.certifications.size() > 3 || emps.contains(this.employer)));
					good = ((this.experience > 10 || this.hasBlog || this.certifications.size() > 3 || employees.contains(this.employer)));
					if (!good) {
						String[] splitted = this.email.split("@");
						String emailDomain = splitted[splitted.length - 1];
						if (!domains.contains(emailDomain) && (!(browser.getName() == WebBrowser.BrowserName.InternetExplorer && browser.getMajorVersion() < 9)))
						{
							good = true;
						}
					}
					
					if (good) {
						if (this.sessions.size() != 0) {
							for (Session session : sessions) {
								for (String technology : myArray) {
									if (session.getTitle().contains(technology) || session.getDescription().contains(technology)) {	
										session.setApproved(false);
										break;
									} else {
										session.setApproved(true);
										appr = true;
									}
								}
								
							}
						} else {
							throw new IllegalArgumentException("Can't register speaker with no sessions to present.");
						}
						
						if (appr) {
							if (this.experience <= 1) {
								this.registrationFee = 500;
							}
							//else if (exp >= 2 && exp <= 3) {
							else if (experience >= 2 && experience <= 3) {
								this.registrationFee = 250;
							}
							//else if (exp >= 4 && exp <= 5) {
							else if (experience >= 4 && experience <= 5) {
								this.registrationFee = 100;
							}
							//else if (exp >= 6 && exp <= 9) {
							else if (experience >= 6 && experience <= 9) {
								this.registrationFee = 50;
							}
							else {
								this.registrationFee = 0;
							}
							
							//Now, save the speaker and sessions to the db.
							try {
								speakerId = repository.saveSpeaker(this);
							} catch (Exception e) {
								//in case the db call fails 
							}
						} else {
							throw new NoSessionsApprovedException("No sessions approved.");
						}
					} else {
						throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our abitrary and capricious standards.");
					}
				} else {
					throw new IllegalArgumentException("Email is required.");
				}				
			} else {
				throw new IllegalArgumentException("Last name is required.");
			}			
		} else {
			throw new IllegalArgumentException("First Name is required");
		}
			
		//if we got this far, the speaker is registered.
		return speakerId;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExp() {
		//return exp;
		return experience;
	}

	//public void setExp(int exp) {
	//	this.exp = exp;
	//}

	public void setExp(int experience) {
			this.experience = experience;
		}	
	
	public boolean isHasBlog() {
		return hasBlog;
	}

	public void setHasBlog(boolean hasBlog) {
		this.hasBlog = hasBlog;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(WebBrowser browser) {
		this.browser = browser;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(int registrationFee) {
		this.registrationFee = registrationFee;
	}
}