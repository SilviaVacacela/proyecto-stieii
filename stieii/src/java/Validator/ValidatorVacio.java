/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author KathyR
 */

@FacesValidator("validatorVacio")
public class ValidatorVacio implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String label;
        HtmlInputText htmlInputText= (HtmlInputText) component;
        
        if(htmlInputText.getLabel()==null || htmlInputText.getLabel().trim().equals("")){
            label=htmlInputText.getId();
        }else{
            label=htmlInputText.getLabel();
        }
        
        if(value.toString().trim().equals("")){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label+ ": es un campo requerido"));
        }
    }
    
}
