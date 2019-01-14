package io.sesam.fredrikstad.demo.soap;

import io.sesam.fredrikstad.demo.AppConfig;
import io.sesam.fredrikstad.demo.models.Address;
import io.sesam.fredrikstad.demo.models.ConnectionAgreement;
import io.sesam.fredrikstad.demo.models.Customer;
import io.sesam.fredrikstad.demo.models.CustomerClassification;
import io.sesam.fredrikstad.demo.models.CustomerPropertyAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.sesam.fredrikstad.demo.models.EmailAddress;
import io.sesam.fredrikstad.demo.models.MeterNumber;
import io.sesam.fredrikstad.demo.models.NetworkPropertyLink;
import io.sesam.fredrikstad.demo.models.PhoneNumber;
import io.sesam.fredrikstad.demo.models.Property;
import io.sesam.fredrikstad.demo.models.PropertyClassification;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import poweron.wsdl.ConnectionAgreementItemStc;
import poweron.wsdl.ConnectionAgreementListStc;
import poweron.wsdl.ConnectionAgreements;
import poweron.wsdl.ConnectionAgreementsResponse;
import poweron.wsdl.ConnectionAgreementsResponseStc;
import poweron.wsdl.ConnectionAgreementsStc;
import poweron.wsdl.CustomerItemStc;
import poweron.wsdl.CustomerListStc;
import poweron.wsdl.Customers;
import poweron.wsdl.CustomersResponse;
import poweron.wsdl.CustomersResponseStc;
import poweron.wsdl.CustomersStc;
import poweron.wsdl.EmailAddressItemStc;
import poweron.wsdl.EmailAddressListStc;
import poweron.wsdl.EmailAddresses;
import poweron.wsdl.EmailAddressesResponse;
import poweron.wsdl.EmailAddressesResponseStc;
import poweron.wsdl.EmailAddressesStc;
import poweron.wsdl.ObjectFactory;

/**
 * Simple SoapService client for Power ON customer inbound messages service
 *
 * @author 100tsa
 */
@Component
public class PowerOnSoapClient extends WebServiceGatewaySupport {

    @Autowired
    AppConfig config;

    private static final ObjectFactory FACTORY = new ObjectFactory();
    private static final Logger LOG = LoggerFactory.getLogger(PowerOnSoapClient.class);

    /**
     * Insert/update email addresses
     *
     * @param input list of email addresses
     */
    public void processEmailAddressList(List<EmailAddress> input) {
        EmailAddresses soapMessage = FACTORY.createEmailAddresses();
        EmailAddressesStc emailAddressesPlaceholder = FACTORY.createEmailAddressesStc();
        emailAddressesPlaceholder.setOperationType("I");
        EmailAddressListStc emailList = FACTORY.createEmailAddressListStc();
        List<EmailAddressItemStc> emailAddressStc = emailList.getEmailAddressStc();

        input.stream().map((emailAddress) -> {
            EmailAddressItemStc item = FACTORY.createEmailAddressItemStc();
            item.setCustomerNumber(emailAddress.getCustomerNumber());
            item.setEmailAddress(emailAddress.getEmailAddress());
            emailAddress.setStatus("Successfully added to queue.");
            return item;
        }).forEachOrdered(emailAddressStc::add);
        emailAddressesPlaceholder.setEmailAddressList(emailList);
        soapMessage.setEmailAddressesStc(emailAddressesPlaceholder);

        WebServiceTemplate template = buildWebServiceTemplate();

        EmailAddressesResponse res = (EmailAddressesResponse) template.marshalSendAndReceive(config.getUrl(), soapMessage,
                new SoapActionCallback("Customer/EmailAddresses"));
        EmailAddressesResponseStc innerRes = res.getEmailAddressesResponseStc();
        LOG.info("status: {}, message: {}", innerRes.getStatus(), innerRes.getTransactionErrors());
        if (isNotOk(innerRes.getStatus())) {
            throw new OperationException(innerRes.getTransactionErrors());
        }

    }

    /**
     * Insert/update addresses
     *
     * @param input
     */
    public void processAddresses(List<Address> input) {
        for (Address address : input) {
            //FIXME, fix json mapping in Address model before
        }
    }

    /**
     * Insert/update connection agreements
     *
     * @param input
     */
    public void processConnectionAgreements(List<ConnectionAgreement> input) {
        ConnectionAgreements connectionAgreements = FACTORY.createConnectionAgreements();
        ConnectionAgreementsStc connectionAgreementsStc = FACTORY.createConnectionAgreementsStc();
        connectionAgreementsStc.setOperationType("I");
        ConnectionAgreementListStc connectionAgreementListStc = FACTORY.createConnectionAgreementListStc();
        List<ConnectionAgreementItemStc> agreements = connectionAgreementListStc.getConnectionAgreementStc();

        for (ConnectionAgreement conAgreement : input) {
            ConnectionAgreementItemStc item = FACTORY.createConnectionAgreementItemStc();
            if (!conAgreement.getAgreementStartDate().isEmpty()) {
                try {
                    item.setAgreementStartDate(FACTORY.createConnectionAgreementItemStcAgreementStartDate(
                            this.parseDateStringToXmlGregorianCalendar(conAgreement.getAgreementStartDate(), null)));
                } catch (DatatypeConfigurationException ex) {
                    LOG.error("Couldn't parse date {} to XMLGregorianCalendar", conAgreement.getAgreementStartDate());
                    LOG.error(ex.getMessage());
                }
            }
            item.setCustomerNumber(FACTORY.createConnectionAgreementItemStcCustomerNumber(
                    conAgreement.getCustomerNumber()));
            item.setNoticeToDeenergise(FACTORY.createConnectionAgreementItemStcNoticeToDeenergise(
                    conAgreement.getNoticeToDeenergise()));
            item.setPropertyNumber(FACTORY.createConnectionAgreementItemStcPropertyNumber(
                    conAgreement.getPropertyNumber()));
            agreements.add(item);
        }
        connectionAgreementsStc.setConnectionAgreementList(connectionAgreementListStc);
        connectionAgreements.setConnectionAgreementsStc(connectionAgreementsStc);
        WebServiceTemplate template = buildWebServiceTemplate();
        ConnectionAgreementsResponse res
                = (ConnectionAgreementsResponse) template.marshalSendAndReceive(config.getUrl(), connectionAgreements,
                        new SoapActionCallback("Customer/ConnectionAgreements"));
        ConnectionAgreementsResponseStc innerRes = res.getConnectionAgreementsResponseStc();
        LOG.info("status: {}, message: {}", innerRes.getStatus(), innerRes.getTransactionErrors());
        if (isNotOk(innerRes.getStatus())) {
            throw new OperationException(innerRes.getTransactionErrors());
        }
    }

    /**
     * Insert/update customer property associations
     *
     * @param input
     */
    public void processCustomerPropertyAssociations(List<CustomerPropertyAssociation> input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Insert/update customers
     *
     * @param input
     */
    public void processCustomers(List<Customer> input) {
        Customers customers = FACTORY.createCustomers();
        CustomersStc customersStc = FACTORY.createCustomersStc();
        customersStc.setOperationType("I");
        CustomerListStc customerListStc = FACTORY.createCustomerListStc();
        List<CustomerItemStc> customerList = customerListStc.getCustomerStc();

        for (Customer customer : input) {
            CustomerItemStc item = FACTORY.createCustomerItemStc();
            item.setCustomerNumber(customer.getCustomerNumber());
            item.setForeNames(FACTORY.createCustomerItemStcForeNames(customer.getForeName()));
            item.setName(FACTORY.createCustomerItemStcName(customer.getSurName()));
            customerList.add(item);
        }
        customersStc.setCustomerList(customerListStc);
        customers.setCustomersStc(customersStc);
        WebServiceTemplate template = buildWebServiceTemplate();
        CustomersResponse res = (CustomersResponse) template.marshalSendAndReceive(config.getUrl(), customers,
                new SoapActionCallback("Customer/Customers"));
        CustomersResponseStc innerRes = res.getCustomersResponseStc();
        LOG.info("status: {}, message: {}", innerRes.getStatus(), innerRes.getTransactionErrors());
        if (isNotOk(innerRes.getStatus())) {
            throw new OperationException(innerRes.getTransactionErrors());
        }
    }

    /**
     * Insert/update customer classifications
     *
     * @param input
     */
    public void processCustomerClassifications(List<CustomerClassification> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insert/update meter point numbers
     *
     * @param input
     */
    public void processMeterNumbers(List<MeterNumber> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insert/update properties
     *
     * @param input
     */
    public void processProperties(List<Property> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insert/update property classifications
     *
     * @param input
     */
    public void processPropertyClassifications(List<PropertyClassification> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Insert/update phone numbers
     *
     * @param input
     */
    public void processPhoneNumbers(List<PhoneNumber> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param input
     */
    public void processNetworkPropertyLinks(List<NetworkPropertyLink> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * utility function to build and return web service template
     *
     * @return
     */
    private WebServiceTemplate buildWebServiceTemplate() {
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setContextPath("poweron.wsdl");
        WebServiceTemplate template = getWebServiceTemplate();
        template.setMarshaller(m);
        template.setUnmarshaller(m);
        return template;
    }

    /**
     * utility funciton to convert date string into XMLGregorianCalender instance
     *
     * @param date date (time) string
     * @param format not used yet
     * @return instance of XMLGregorianCalendar for given date
     * @throws DatatypeConfigurationException
     */
    private XMLGregorianCalendar parseDateStringToXmlGregorianCalendar(String date, String format) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
    }

    /**
     * function to check power on response status code
     *
     * @param i PowerOn status code as int
     * @return boolean true if status not equal to success code (which is 0)
     */
    private boolean isNotOk(int i) {
        return i != 0;
    }
}