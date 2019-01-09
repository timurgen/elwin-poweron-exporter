package io.sesam.fredrikstad.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.sesam.fredrikstad.demo.models.Address;
import io.sesam.fredrikstad.demo.models.ConnectionAgreement;
import io.sesam.fredrikstad.demo.models.Customer;
import io.sesam.fredrikstad.demo.models.CustomerClassification;
import io.sesam.fredrikstad.demo.models.CustomerPropertyAssociation;
import io.sesam.fredrikstad.demo.models.EmailAddress;
import io.sesam.fredrikstad.demo.models.MeterNumber;
import io.sesam.fredrikstad.demo.models.NetworkPropertyLink;
import io.sesam.fredrikstad.demo.models.PhoneNumber;
import io.sesam.fredrikstad.demo.models.Property;
import io.sesam.fredrikstad.demo.models.PropertyClassification;
import io.sesam.fredrikstad.demo.soap.PowerOnSoapClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Main app controller
 *
 * @author TImur Samkharadze
 */
@RestController
public class RequestProcessor {

    @Autowired
    PowerOnSoapClient wsClient;

    private static final Logger LOG = LoggerFactory.getLogger(RequestProcessor.class);
    private static final String CT = "application/json";
    /**
     * REST endpoint for elwin-to-power-on-addresses pipe
     * @param input list of Address objects each containg pipe output entity
     * @return 
     */
    @RequestMapping(value = "/cd_addresses", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<Address> cdAddresses(@RequestBody List<Address> input) {
        LOG.info("request to cd_addresses endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processAddresses(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_connection_agreement", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<ConnectionAgreement> cdConnectionAgreement(@RequestBody List<ConnectionAgreement> input) {
        LOG.info("request to cd_connection_agreement endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processConnectionAgreements(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_cust_property_assoc", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<CustomerPropertyAssociation> cdCustPropertyAssoc(@RequestBody List<CustomerPropertyAssociation> input) {
        LOG.info("request to cd_cust_property_assoc endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processCustomerPropertyAssociations(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_customer", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<Customer> cdCustomer(@RequestBody List<Customer> input) {
        LOG.info("request to cd_customer endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processCustomers(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_customer_classifications", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<CustomerClassification> cdCustomerClassifications(@RequestBody List<CustomerClassification> input) {
        LOG.info("request to cd_customer_classifications endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processCustomerClassifications(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_email_addr", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<EmailAddress> cdEmailAddr(@RequestBody List<EmailAddress> input) {
        LOG.info("request to cd_email_addr endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processEmailAddressList(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_meter_no", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<MeterNumber> cdMeterNo(@RequestBody List<MeterNumber> input) {
        LOG.info("request to cd_meter_no endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processMeterNumbers(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_properties", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<Property> cdProperties(@RequestBody List<Property> input) {
        LOG.info("request to cd_properties endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processProperties(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_property_classifications", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<PropertyClassification> cdPropertyClassifications(@RequestBody List<PropertyClassification> input) {
        LOG.info("request to cd_property_classifications endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processPropertyClassifications(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/cd_telephone_no", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<PhoneNumber> cdTelephoneNo(@RequestBody List<PhoneNumber> input) {
        LOG.info("request to cd_telephone_no endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processPhoneNumbers(input);
        return input;
    }
    /**
     * 
     * @param input
     * @return 
     */
    @RequestMapping(value = "/network_property_link_new", method = {RequestMethod.POST}, consumes = CT, produces = CT)
    public List<NetworkPropertyLink> networkPropertyLinkNew(@RequestBody List<NetworkPropertyLink> input) {
        LOG.info("request to network_property_link_new endpoint with batch size {}", input.size());
        if (0 == input.size()) {
            LOG.info("Empty set, no processing needed, return.");
            return input;
        }
        this.wsClient.processNetworkPropertyLinks(input);
        return input;
    }

}
