package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    void create(Profile profile);
    Profile getProfileById(int userId);
    void saveOrUpdate(Profile profile);
    void delete(Profile profile);
}
