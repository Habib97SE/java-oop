package habhez0;

import java.util.ArrayList;

public class Customer
{
   private String firstName;
   private String lastName;
   private final String socialSecurityNumber;


   public Customer()
   {
      this.firstName = "";
      this.lastName = "";
      this.socialSecurityNumber = "";
   }
   public Customer (String firstName, String lastName, String socialSecurityNumber)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.socialSecurityNumber = socialSecurityNumber;

   }

   public void setFirstName (String firstName)
   {
      this.firstName = firstName;
   }

   public void setLastName (String lastName)
   {
      this.lastName = lastName;
   }

   public String getSocialSecurityNumber ()
   {
      return this.socialSecurityNumber;
   }

   public String getFirstName ()
   {
      return this.firstName;
   }


   public String getLastName ()
   {
      return this.lastName;
   }

   /**
    * @return the full name of the customer in the format: firstName lastName
    */
   public String getFullName ()
   {
      return this.firstName + " " + this.lastName;
   }

   public String toString ()
   {
      return firstName + " " + lastName + " " + socialSecurityNumber;
   }
}
