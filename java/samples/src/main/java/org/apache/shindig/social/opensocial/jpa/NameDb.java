/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.opensocial.jpa;

import static javax.persistence.GenerationType.IDENTITY;

import org.apache.shindig.social.opensocial.jpa.api.DbObject;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.Person;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;

/**
 * The name object, stored in the name table.
 */
@Entity
@Table(name = "name")
@NamedQuery(name = NameDb.FINDBY_FAMILY_NAME, query = "select n from NameDb n where n.familyName = :familyName ")
public class NameDb implements Name, DbObject {
  /**
   * the name of the JPA query that selects a name by family name.
   */
  public static final String FINDBY_FAMILY_NAME = "q.name.findbyfamilyname";
  /**
   * the name of the family name parameter used in JPA named queries.
   */
  public static final String PARAM_FAMILY_NAME = "familyName";

  /**
   * The internal object ID used for references to this object. Should be generated by the
   * underlying storage mechanism
   */
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "oid")
  private long objectId;

  /**
   * An optimistic locking field
   */
  @Version
  @Column(name = "version")
  protected long version;

  /**
   * A list of people who shared this name, relationship is specified by the name property on the
   * Person Object.
   */
  @OneToMany(targetEntity = PersonDb.class, mappedBy = "name")
  private List<Person> persons;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "additional_name", length = 255)
  private String additionalName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "family_name", length = 255)
  private String familyName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "given_name", length = 255)
  private String givenName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "honorific_prefix", length = 255)
  private String honorificPrefix;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "honorific_suffix", length = 255)
  private String honorificSuffix;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "unstructured", length = 255)
  private String unstructured;

  /**
   *
   */
  public NameDb() {
  }

  /**
   * @param unstructured
   */
  public NameDb(String unstructured) {
    this.unstructured = unstructured;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getAdditionalName()
   */
  public String getAdditionalName() {
    return additionalName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setAdditionalName(java.lang.String)
   */
  public void setAdditionalName(String additionalName) {
    this.additionalName = additionalName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getFamilyName()
   */
  public String getFamilyName() {
    return familyName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setFamilyName(java.lang.String)
   */
  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getGivenName()
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setGivenName(java.lang.String)
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getHonorificPrefix()
   */
  public String getHonorificPrefix() {
    return honorificPrefix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setHonorificPrefix(java.lang.String)
   */
  public void setHonorificPrefix(String honorificPrefix) {
    this.honorificPrefix = honorificPrefix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getHonorificSuffix()
   */
  public String getHonorificSuffix() {
    return honorificSuffix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setHonorificSuffix(java.lang.String)
   */
  public void setHonorificSuffix(String honorificSuffix) {
    this.honorificSuffix = honorificSuffix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.jpa.api.DbObject#getObjectId()
   */
  public long getObjectId() {
    return objectId;
  }

  /**
   * A list of people who have this name
   *
   * @return the persons
   */
  public List<Person> getPersons() {
    return persons;
  }

  /**
   * Set the list of people who have this name
   *
   * @param persons the persons to set
   */
  public void setPersons(List<Person> persons) {
    this.persons = persons;

  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Name#getUnstructured()
   */
  public String getUnstructured() {
    return unstructured;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Name#setUnstructured(java.lang.String)
   */
  public void setUnstructured(String unstructured) {
    this.unstructured = unstructured;
  }
}
