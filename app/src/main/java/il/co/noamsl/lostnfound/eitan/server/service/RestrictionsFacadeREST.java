package il.co.noamsl.lostnfound.eitan.server.service;

import il.co.noamsl.lostnfound.eitan.server.FoundTable;
import il.co.noamsl.lostnfound.eitan.server.LostTable;
import il.co.noamsl.lostnfound.eitan.server.Restrictions;
import il.co.noamsl.lostnfound.eitan.server.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("restrict")
public class RestrictionsFacadeREST extends AbstractFacade<Restrictions> {

    @PersistenceContext(unitName = "lf_serverPU")
    private EntityManager em;

    public RestrictionsFacadeREST() {
        super(Restrictions.class);
    }

    /**
     * Adds a new Restrictions entry for a given record
     * @param entity    New entry to be added
     * @return  LFCode.SUCCESS on success, negative error code on failure
     */
    @POST
    @Override
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer create(Restrictions entity) {
        if (null == entity.getRecordid()) {
            return LFCode.BAD_PARAMS;
        }
        
        if (isRestrictionExists(entity.getRecordid())) {
            return LFCode.EXISTS;
        }
        
        if (!isRecordExists(entity.getRecordid())) {
            return LFCode.NOT_EXISTS;
        }
        
        entity.setDefault();
        
        super.create(entity);
        
        return LFCode.SUCCESS;
    }
    
    /**
     * Updates an existing Restrictions record
     * @param   entity    Record with updated fields
     * @return  LFCode.SUCCESS success, negative error code on failure
     * @note    Only updates non-null fields in entity
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer edit(Restrictions entity) {
        if (null == entity.getRecordid()) { 
            return LFCode.BAD_PARAMS;
        }
        if (!isRestrictionExists(entity.getRecordid())){
            return LFCode.NOT_EXISTS;
        }
        
        Restrictions r = em.find(Restrictions.class, entity.getRecordid());
        r.update(entity);
        
        return LFCode.SUCCESS;
    }
    
    private boolean isRestrictionExists(Integer id) {
        List l = em.createNamedQuery("Restrictions.findByRecordid")
                .setParameter("recordid", id)
                .getResultList();
        return !l.isEmpty();
    }
    
    private boolean isRecordExists(Integer id) {
        List l = em.createNamedQuery("LostTable.findByRecordid")
                .setParameter("recordid", id)
                .getResultList();
        List f = em.createNamedQuery("FoundTable.findByRecordid")
                .setParameter("recordid", id)
                .getResultList();
        return !l.isEmpty() || !f.isEmpty();
    }
    
    private Users getOwnerByRecordId(Integer recordid) {
        Integer userid = null;
        List l = em.createNamedQuery("LostTable.findByRecordid")
                .setParameter("recordid", recordid)
                .getResultList();
        if (!l.isEmpty()) {
            userid = ((LostTable)l.get(0)).getOwner();
        } else {
            List f = em.createNamedQuery("FoundTable.findByRecordid")
                .setParameter("recordid", recordid)
                .getResultList();
            if (!f.isEmpty()) {
                userid = ((FoundTable)f.get(0)).getOwner();
            }
        }
        
        if (null == userid) {
            return null;
        }
        
        List u = em.createNamedQuery("Users.findByUserid")
            .setParameter("userid", userid)
            .getResultList();
        if (u.isEmpty()) {
            return null;
        }
        
        return (Users)u.get(0);
    }
    
    /**
     * Returns a user with only the allowed fields present
     * @param recordid  Record id to look by
     * @return          Users object, or null on failure
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Users getAllowedUserData(@QueryParam("recordid") Integer recordid) {
        if (null == recordid) {
            return null;
        }
        
        Restrictions r = super.find(recordid);
        if (null == r) {
            return null;
        }
        
        Users u = getOwnerByRecordId(recordid);
        if (null == u) {
            return null;
        }
        
        if (!r.getAddress()) {
            u.setAddress(null);
        }
        if (!r.getEmail()) {
            u.setEmail(null);
        }
        if (!r.getName()) {
            u.setName(null);
        }
        if (!r.getPhoneNumber()) {
            u.setPhoneNumber(null);
        }
        
        return u;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
