import com.sendgrid.*;

public class EmailVerification {

    private static final String SENDGRID_API_KEY = "YOUR_SENDGRID_API_KEY";

    public static void sendVerificationEmail(String userEmail, String verificationCode) throws IOException {
        Email from = new Email("your-email@example.com");
        String subject = "Verify Your Transaction";
        Email to = new Email(userEmail);
        Content content = new Content("text/plain", "Your verification code is: " + verificationCode);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static boolean verifyCode(String userEnteredCode, String sentCode) {
        return userEnteredCode.equals(sentCode);
    }
}
