
# Ecommerce (Backend)
This project has the goal to establish the knowledge that I’m getting about Spring boot, Spring security, Docker and Microservices.
The way that I will do this project is by versions, so every version will have some new features and of course new technologies.

The project is about a simple API to an ecommerce where the customers could register and buy products.

## Configure the project if you want to use it

you have to create an account in Twilio, Stripe and in Google cloud.
Then add in your application.properties:
  - for twilio the account,token and phone that twilio give you
  - for Stripe the public.key and secret.key that stripe give you
  - for Google oauth2 the client-id, client-secret and the scope

Important to know that you have to save the customers with a real phone number to receive the SMS.


## versions
The current version is v4.

**v2**: added some exceptions, the payment class, implementation of pagination and resent the email confirmation when it is expired. 

**v1**: created project’s bases, security based authentication and authorization,implementation of JWT, email confirmation to register and service and repository layers tested.

**v3**: OAuth2 with Google added in order the customers can make login with its Google account

**v4**: Payment with Stripe (create,confirm and cancel) , notify customers, export to both excel and pdf and store picture of customer and product stock

## Tools and dependencies
* Java 17
* Spring boot 3
* Spring web
* Spring jpa
* Spring security 6
* Spring mail
* H2
* Lombok
* Jwt
* Intellij Idea
* poi
* librepdf/openpdf
* stripe
* twilio
* [Maildev](https://github.com/maildev/maildev/tree/master) 
