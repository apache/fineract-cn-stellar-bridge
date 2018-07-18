package org.apache.fineract.cn.stellarbridge.service.internal.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@SuppressWarnings("unused")
@Entity
@Table(name = "nenet_stellar_cursor")
public class StellarCursorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "xcursor")
  private String cursor;

  @SuppressWarnings("unused")
  @Column(name = "processed")
  private Boolean processed;

  @Column(name = "created_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @SuppressWarnings("unused")
  public StellarCursorEntity() { }

  public StellarCursorEntity(final String cursor, final Date createdOn) {
    this.cursor = cursor;
    this.processed = false;
    this.createdOn = createdOn;
  }

  public String getCursor() {
    return cursor;
  }

  public void setProcessed(Boolean processed) {
    this.processed = processed;
  }

  public Boolean getProcessed() {
    return processed;
  }

  @SuppressWarnings("unused")
  public Date getCreatedOn() {
    return createdOn;
  }

  @SuppressWarnings("unused")
  public void setCreatedOn(final Date createdOn) {
    this.createdOn = createdOn;
  }
}
