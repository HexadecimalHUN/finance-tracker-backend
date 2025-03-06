package org.acme.demo.service;

import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Limit;
import org.acme.demo.entity.PredefinedLimit;
import org.acme.demo.repository.LimitRepository;
import org.acme.demo.repository.PredefinedLimitRepository;
import org.acme.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final PredefinedLimitRepository predefinedLimitRepository;

    @Autowired
    public LimitService(LimitRepository limitRepository, PredefinedLimitRepository predefinedLimitRepository){
        this.limitRepository = limitRepository;
        this.predefinedLimitRepository = predefinedLimitRepository;

    };

    public Limit getUserLimit(AppUser user){
        return limitRepository.findByUserId(user.getId());
    }

    /**
     * Sets or updates a custom spending limit for the user.
     * @param user The user for whom the limit is being set.
     * @param amount The custom limit amount.
     * @return The saved limit entity.
     */

    public Limit setOrUpdateCustomLimit(AppUser user, BigDecimal amount){

        Limit limit = limitRepository.findByUserId(user.getId());
        if (limit == null){
            limit = new Limit();
            limit.setUser(user);
        }
        limit.setAmount(amount);
        limit.setPredefined(false);
        limit.setPredefinedId(null);
        return limitRepository.save(limit);
    }

    /**
     * Sets a predefined spending limit for the user.
     * @param user The user for whom the limit is being set.
     * @param predefinedLimitId The ID of the predefined limit.
     * @return The saved limit entity, or null if the predefined limit was not found.
     */
    public Limit setPredefinedLimit(AppUser user, Long predefinedLimitId){
        Optional<PredefinedLimit> predefinedLimitOPT = predefinedLimitRepository.findById(predefinedLimitId);
        if (predefinedLimitOPT.isEmpty()){
            return null;
        }

        PredefinedLimit predefinedLimit = predefinedLimitOPT.get();
        Limit limit = limitRepository.findByUserId(user.getId());
        if(limit == null){
            limit = new Limit();
            limit.setUser(user);
        }
        limit.setAmount(predefinedLimit.getAmount());
        limit.setPredefined(true);
        limit.setPredefinedId(predefinedLimitId);
        return limitRepository.save(limit);
    }

    /**
     * Deletes the user's spending limit.
     * @param user The user for whom the limit is being deleted.
     */
    public void deleteLimit(AppUser user){
        Limit limit = limitRepository.findByUserId(user.getId());
        if(limit != null){
            limitRepository.delete(limit);
        }
    }
}
