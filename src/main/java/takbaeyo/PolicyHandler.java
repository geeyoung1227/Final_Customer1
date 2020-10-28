package takbaeyo;

import org.springframework.beans.factory.annotation.Autowired;
import takbaeyo.config.kafka.KafkaProcessor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    CustomerRepository customerRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReqCanceledCoupon_CouponPol(@Payload ReqCanceledCoupon reqCanceledCoupon){

        if(reqCanceledCoupon.isMe()){
            Iterator<Customer> iterator = customerRepository.findAll().iterator();
            while(iterator.hasNext()){
                Customer customerTmp = iterator.next();
                if(customerTmp.getRequestId() == reqCanceledCoupon.getId()){
                    Optional<Customer> CustomerOptional = customerRepository.findById(customerTmp.getId());
                    Customer customer = CustomerOptional.get();
                    customer.setStatus(reqCanceledCoupon.getStatus());
                    customerRepository.save(customer);
                }
            }
            System.out.println("##### listener CouponPol : " + reqCanceledCoupon.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDelivered_CouponPol(@Payload Delivered delivered){

        if(delivered.isMe()){
            Iterator<Customer> iterator = customerRepository.findAll().iterator();
            while(iterator.hasNext()){
                Customer customerTmp = iterator.next();
                if((customerTmp.getMemberId() == delivered.getMemberId()) && delivered.getStatus().equals("Finish")){
                    Optional<Customer> CustomerOptional = customerRepository.findById(customerTmp.getId());
                    Customer customer = CustomerOptional.get();
                    customer.setRequest(customer.getRequest()+1);
                    customerRepository.save(customer);
                }
            }
        }
    }

}
