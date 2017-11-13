package il.co.noamsl.lostnfound.eitan.server.service;

import il.co.noamsl.lostnfound.eitan.server.FoundTable;
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
@Path("found")
public class FoundTableFacadeREST extends AbstractFacade<FoundTable> {

    @PersistenceContext(unitName = "lf_serverPU")
    private EntityManager em;

    public FoundTableFacadeREST() {
        super(FoundTable.class);
    }

    /**
     * Adds a new Found entry
     * @param entity    New entry to be added
     * @return  New entry's ID on success, negative error code on failure
     */
    @POST
    @Override
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer create(FoundTable entity) {
        if (null == entity.getOwner()) {
            return LFCode.BAD_PARAMS;
        }
        
        if (null == entity.getName() || null == entity.getDescription()) {
            return LFCode.BAD_PARAMS;
        }
        
        Integer id = getNextRecordId();
        entity.setRecordid(id);
        entity.setRelevant(Boolean.TRUE);
        
        super.create(entity);
        
        return id;
    }
    
    private Integer getNextRecordId() {
        List l = em.createNamedQuery("LostTable.getMaxRecordId")
                .getResultList();
        List f = em.createNamedQuery("FoundTable.getMaxRecordId")
                .getResultList(); 
        if (l.isEmpty() && f.isEmpty()) {
            return 1;
        }
        Integer maxL = (Integer)l.get(0);
        Integer maxF = (Integer)f.get(0);
        return (maxL > maxF ? maxL : maxF) + 1;
    }
    
    /**
     * Updates an existing record
     * @param   entity    Record with updated fields
     * @return  LFCode.SUCCESS success, negative error code on failure
     * @note    Only updates non-null fields in entity
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer edit(FoundTable entity) {
        if (null == entity.getRecordid()) { 
            return LFCode.BAD_PARAMS;
        }
        if (!isRecordExists(entity.getRecordid())){
            return LFCode.NOT_EXISTS;
        }
        
        FoundTable f = em.find(FoundTable.class, entity.getRecordid());
        f.update(entity);
        
        return LFCode.SUCCESS;
    }
    
    private boolean isRecordExists(Integer id) {
        List l = em.createNamedQuery("FoundTable.findByRecordid")
                .setParameter("recordid", id)
                .getResultList();
        return !l.isEmpty();
    }
    
    /**
     * Gets all relevant Found records with name like the given name, 
     * or description like the given description, or location
     * @param name          Item name to look by
     * @param description   Description name to look by
     * @param location      Location to look by
     * @return              List of FoundTable
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<FoundTable> getFoundItems(@QueryParam("name") String name,
            @QueryParam("description") String description,
            @QueryParam("location") String location) {
        if (null == name && null == description && null == location) {
            return null;    
        }
        
        String baseQuery = "SELECT f FROM FoundTable f WHERE 1=1";
        if (null != name) {
            baseQuery += " AND LOWER(f.name) LIKE LOWER('%" + name + "%')";
        } 
        if (null != description) {
            baseQuery += " AND LOWER(f.description) LIKE LOWER('%" + description + "%')";
        } 
        if (null != location) {
            baseQuery += " AND LOWER(f.location) LIKE LOWER('%" + location + "%')";
        } 
        
        List l = em.createQuery(baseQuery).getResultList();
        for (Object o : l) {
            FoundTable r = (FoundTable)o;
            if (!r.getRelevant()) {
                l.remove(o);
            }
        }
        
        return l;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
