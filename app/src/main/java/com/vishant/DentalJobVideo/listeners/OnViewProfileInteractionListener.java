package com.vishant.DentalJobVideo.listeners;

import com.vishant.DentalJobVideo.model.JobSeekerProfileEducationModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileExperienceModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileSkillsModel;

/**
 * Created by Sultan Ahmed on 4/13/2017.
 */

public interface OnViewProfileInteractionListener {
    void onAddSkill();
    void onEditSkill(JobSeekerProfileSkillsModel skill);
    void onAddObjective();
    void onEditObjective(String objective);
    void onAddEducation();
    void onEditEducation(JobSeekerProfileEducationModel education);
    void onAddExperience();
    void onEditExperience(JobSeekerProfileExperienceModel experience);
    void onPlayProfileVideo(String videoUri);
    void updateProfileVideo(String videoUrl);
    void addProfileVideo();
    void updateUserProfile(JobSeekerProfileModel profile);
}
