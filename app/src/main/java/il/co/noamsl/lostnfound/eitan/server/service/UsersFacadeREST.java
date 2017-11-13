package il.co.noamsl.lostnfound.eitan.server.service;

import il.co.noamsl.lostnfound.eitan.server.FoundTable;
import il.co.noamsl.lostnfound.eitan.server.LostTable;
import il.co.noamsl.lostnfound.eitan.server.Users;
import java.util.ArrayList;
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
@Path("user")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "lf_serverPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }
    
    /**
     * Adds a new user
     * @param entity    New User to be added
     * @return  New user's UserID on success, negative error code on failure
     */
    @POST
    @Override
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer create(Users entity) {
        if (null == entity.getEmail()) {
            return LFCode.BAD_PARAMS;
        }
        
        if (isUserExists(entity.getEmail())) {
            return LFCode.EXISTS;
        }
                
        if (!entity.validateEmail()) {
            return LFCode.BAD_PARAMS;
        }
        
        Integer id = getNextUserId();
        entity.setUserid(id);
        
        super.create(entity);
        
        return id;
    }
    
    private boolean isUserExists(String email) {
        List l = em.createNamedQuery("Users.findByEmail")
                .setParameter("email", email)
                .getResultList();
        return !l.isEmpty();
    }
    
    private boolean isUserExists(Integer id) {
        List l = em.createNamedQuery("Users.findByUserid")
                .setParameter("userid", id)
                .getResultList();
        return !l.isEmpty();
    }
    
    private Integer getNextUserId() {
        List l = em.createNamedQuery("Users.getMaxUserId")
                .getResultList();
        if (l.isEmpty()) {
            return 1;
        } 
        return (Integer)l.get(l.size() - 1) + 1;
    }
    
    /**
     * Updates an existing user
     * @param   entity    User with updated fields
     * @return  LFCode.SUCCESS success, negative error code on failure
     * @note    Only updates non-null fields in entity
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer edit(Users entity) {
        if (null == entity.getUserid()) { 
            if (null == entity.getEmail()) {
                return LFCode.BAD_PARAMS;
            }
            
            Integer id = getIdByEmail(entity.getEmail());
            if (null == id) {
                return LFCode.NOT_EXISTS;
            }
            
            entity.setUserid(id);
        } else if (!isUserExists(entity.getUserid())) {
            return LFCode.NOT_EXISTS;
        }
        
        if (null != entity.getEmail() && !entity.validateEmail()) {
            return LFCode.BAD_PARAMS;
        }
        
        Users u = em.find(Users.class, entity.getUserid());
        u.update(entity);
        
        return LFCode.SUCCESS;
    }
    
    public Integer getIdByEmail(String email) {
        List l = em.createNamedQuery("Users.getIdByEmail")
                .setParameter("email", email)
                .getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return (Integer)l.get(0);
    }
    
    /**
     * Get the user's settings
     * @param id    User's id
     * @return      Users object, or null if not exists or if id is null
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Users getSettings(@QueryParam("id") Integer id) {
        if (null == id) {
            return null;
        }
        return super.find(id);
    }
    
    /**
     * Gets all lost items belonging to specific owner
     * @param id    Owner's id
     * @return      List of LostTable objects
     */
    @GET
    @Path("/lost")
    @Produces(MediaType.APPLICATION_XML)
    public List<LostTable> getLostItems(@QueryParam("id") Integer id) {
        if (null == id) {
            return null;
        }
        return em.createNamedQuery("LostTable.findByOwner")
                .setParameter("owner", id)
                .getResultList();
    }
    
    /**
     * Gets all found items belonging to specific owner
     * @param id    Owner's id
     * @return      List of FoundTable objects
     */
    @GET
    @Path("/found")
    @Produces(MediaType.APPLICATION_XML)
    public List<FoundTable> getFoundItems(@QueryParam("id") Integer id) {
        if (null == id) {
            return new ArrayList<>();
        }
        return em.createNamedQuery("FoundTable.findByOwner")
                .setParameter("owner", id)
                .getResultList();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
