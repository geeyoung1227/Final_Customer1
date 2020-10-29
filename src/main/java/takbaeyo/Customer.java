package takbaeyo;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name="Customer_table")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private String level;
    private Long coupon;
    private Long request;
    private Long requestId;
    private String status;

    @PostPersist
    public void onPostPersist(){
        try {
                Thread.sleep((long) (400 + Math.random() * 300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        
        if("PaidCoupon".equals(status)) {
            PaidCoupon paidCoupon = new PaidCoupon();
            BeanUtils.copyProperties(this, paidCoupon);
            paidCoupon.publishAfterCommit();
        }

        if("Level".equals(status)){
            ChangedGrade changedGrade = new ChangedGrade();
            BeanUtils.copyProperties(this, changedGrade);
            changedGrade.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        CancelPaidCoupon cancelPaidCoupon = new CancelPaidCoupon();
        BeanUtils.copyProperties(this, cancelPaidCoupon);
        cancelPaidCoupon.publishAfterCommit();


    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public Long getCoupon() {
        return coupon;
    }
    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getRequest() {
        return request;
    }
    public void setRequest(Long request) {
        this.request = request;
    }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
