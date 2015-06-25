package workers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import resources.GroupMemberData;

public class GenerateEmail implements Runnable, Serializable {

	private static final long serialVersionUID = 6865927928574291946L;
	
	private String groupName;
	private String groupId;
	private String discussionName;
	
	public GenerateEmail(String groupId, String groupName,
			String discussionName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.discussionName = discussionName;
	}

	@Override
	public void run() {
		this.gatherEmailList();
	}
	
	public void gatherEmailList(){
		
		GatherGroupMembers ggm = new GatherGroupMembers(groupId);
		
		Thread ggmt = new Thread(ggm);
		
		ggmt.run();
		
		Iterator<GroupMemberData> gmIterator = ggm.getMembers().iterator();
		while(gmIterator.hasNext())
		{
			sendEmail(gmIterator.next().getEmailAddress());
		}
		
		
	}
	
	public void sendEmail(String toEmail)
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("csehive@gmail.com", "csehive13");
					}
				  });
		
		try {
			 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("csehive@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toEmail));
			message.setSubject("Discussion Board Updated!");
			message.setText("Dear "+groupName+" Member,"
				+ "\n\n Discussion board has been updated for discussion: "+discussionName);
 
			Transport.send(message);
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		
		
	}

}
