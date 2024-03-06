package com.ccor.ecommerce.service.registration;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.model.dto.CustomerRequestDTO;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.service.INotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImp implements IRegistrationService{

    private CustomerRepository customerRepository;
    private EmailValidator emailValidator;
    private IEmailSender iEmailSender;
    private PasswordEncoder passwordEncoder;
    private ConfirmationTokenServiceImp confirmationTokenService;
    private INotificationService iNotificationService;
    @Autowired
    public RegistrationServiceImp(CustomerRepository customerRepository, EmailValidator emailValidator,
                                  IEmailSender iEmailSender, PasswordEncoder passwordEncoder,
                                  ConfirmationTokenServiceImp confirmationTokenService,
                                  INotificationService iNotificationService) {
        this.customerRepository = customerRepository;
        this.emailValidator = emailValidator;
        this.iEmailSender = iEmailSender;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.iNotificationService = iNotificationService;
    }

    @Override
    @Transactional
    public AuthenticationResponseDTO save(CustomerRequestDTO requestDTO,MultipartFile picture) throws IllegalAccessException {
        if(requestDTO!=null){
            boolean customerExist = customerRepository.findCustomerByEmailAndIsNotDeleted(requestDTO.email()).isPresent();
            boolean isValidEmail = emailValidator.test(requestDTO.email());
            if(customerExist || !isValidEmail){
                throw new IllegalAccessException("email already taken or  email not valid");
            }else{
                Customer customer = Customer.builder()
                        .address(new ArrayList<>())
                        .cards(new ArrayList<>())
                        .history(new History(null,requestDTO.name()+" "+requestDTO.lastName(),new ArrayList<>(),new Date()))
                        .roles(Arrays.asList(Role.CUSTOMER))
                        .tokens(new ArrayList<>())
                        .confirmationTokens(new ArrayList<>())
                        .enable(false)
                        .isDeleted(false)
                        .receiveNotifications(true)
                        .channelNotifications(channels(requestDTO.channels()))
                        .build();
                customer.setName(requestDTO.name());
                customer.setLastName(requestDTO.lastName());
                customer.setCellphone(requestDTO.cellphone());
                customer.setEmail(requestDTO.email());
                customer.setUsername(requestDTO.username());
                customer.setPwd(passwordEncoder.encode(requestDTO.pwd()));
                if(picture!=null){
                    try {
                        customer.setPicturePath(convertImageToBase64Image(picture));
                    } catch (IOException e) {
                        throw new CustomerException("Error saving the customer image, "+e.getLocalizedMessage());
                    }
                }
                customerRepository.save(customer);
                return new AuthenticationResponseDTO(resendConfirmationEmail(customer.getEmail())); //confirmation token
            }

        }else{
            return null;
        }
    }
    private String convertImageToBase64Image(MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    private List<ChannelNotification> channels(List<String> channels){
       if(!channels.isEmpty()){
           return channels.stream().map(channel-> {
               switch (channel){
                   case "EMAIL":
                       return ChannelNotification.EMAIL;
                   case "SMS":
                       return ChannelNotification.SMS;
                   default: return null;
               }
           }).collect(Collectors.toList());
       }else{
           return null;
       }
    }
    public String resendConfirmationEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmailAndIsNotDeleted(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        ConfirmationToken token = generateToken(customer);

        String link = "http://localhost:8080/api/v1/authentication/confirm?token=" + token.getToken();
        iEmailSender.sendEmail(customer.getEmail(), buildEmail(customer.getName(), link),"Confirm your email");
        return token.getToken();
    }
    public ConfirmationToken generateToken(Customer customer) {
        String tokenValue = UUID.randomUUID().toString();
        ConfirmationToken token = ConfirmationToken.builder()
                .token(tokenValue)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .customer(customer)
                .build();
        confirmationTokenService.saveConfirmationToken(token);
        return token;
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token).orElse(null);
        if(confirmationToken!=null){
            if(confirmationToken.getConfirmedAt()!=null){
                return "Email already confirmed";
            }else{
                LocalDateTime expiredAt = confirmationToken.getExpiresAt();
                if(expiredAt.isBefore(LocalDateTime.now())){//is expired
                    resendConfirmationEmail(confirmationToken.getCustomer().getEmail());
                    System.out.println("Token expired, send another one");
                    return "the token was expired so we already send you another one";
                }else{
                    confirmationTokenService.setConfirmedAt(token);
                    Customer customer = confirmationToken.getCustomer();
                    customer.setEnable(true);
                    customerRepository.save(customer);
                    //send email of welcome
                    sendWelcomeNotification(customer);
                    return "Email confirmed successfully";
                }
            }
        }else{
            return "Error confirmation token null";
        }
    }

    private void sendWelcomeNotification(Customer c){
        List<ChannelNotification> channels = c.getChannelNotifications();
        if(channels!=null && !channels.isEmpty()){
            for (ChannelNotification channel: channels ) {
                if(channel.equals(ChannelNotification.EMAIL)){
                    String message = buildEmailWelcomeMessage();
                    iNotificationService.notifyByEmailWelcomeNewUser(c.getEmail(),c.getUsername(), buildEmailWelcomeMessage());
                }
                if(channel.equals(ChannelNotification.SMS)){
                    iNotificationService.notifyBySmsWelcomeNewUser(c.getCellphone(),buildSmsWelcomeMessage());
                }
            }
        }

    }

    private String buildEmailWelcomeMessage(){
       return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Welcome!</p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Thank you for confirming your account. We're excited to have you as part of our community.</p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">If you have any questions or need assistance, feel free to reach out to our support team. Happy shopping!</p>\n" +
                "</div>";
    }
    private String buildSmsWelcomeMessage(){
        return "Welcome!\n" +
                "Thank you for confirming your account. We're excited to have you as part of our community.\n" +
                "If you have any questions or need assistance, feel free to reach out to our support team. Happy shopping!";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
