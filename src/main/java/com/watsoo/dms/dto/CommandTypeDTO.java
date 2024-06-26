package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.CommandType;

public class CommandTypeDTO {
    private Integer id;
    private String name;
    private Date createOn;
    private Date updatedOn;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    public static CommandTypeDTO convertToDTO(CommandType commandType) {
        CommandTypeDTO commandTypeDTO = new CommandTypeDTO();
        commandTypeDTO.setId(commandType.getId());
        commandTypeDTO.setName(commandType.getName());
        commandTypeDTO.setCreateOn(commandType.getCreateOn());
        commandTypeDTO.setUpdatedOn(commandType.getUpdatedOn());
        return commandTypeDTO;
    }
}

