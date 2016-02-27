/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package murugusmsdbui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author smbuthia
 */
@Entity
@Table(name = "sms", catalog = "murugusms", schema = "")
@NamedQueries({
    @NamedQuery(name = "Sms.findAll", query = "SELECT s FROM Sms s"),
    @NamedQuery(name = "Sms.findById", query = "SELECT s FROM Sms s WHERE s.id = :id"),
    @NamedQuery(name = "Sms.findByText", query = "SELECT s FROM Sms s WHERE s.text = :text"),
    @NamedQuery(name = "Sms.findByResponse", query = "SELECT s FROM Sms s WHERE s.response = :response")})
public class Sms implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "text")
    private String text;
    @Basic(optional = false)
    @Column(name = "response")
    private String response;

    public Sms() {
    }

    public Sms(Integer id) {
        this.id = id;
    }

    public Sms(Integer id, String text, String response) {
        this.id = id;
        this.text = text;
        this.response = response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        String oldText = this.text;
        this.text = text;
        changeSupport.firePropertyChange("text", oldText, text);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        String oldResponse = this.response;
        this.response = response;
        changeSupport.firePropertyChange("response", oldResponse, response);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sms)) {
            return false;
        }
        Sms other = (Sms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "murugusmsdbui.Sms[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
