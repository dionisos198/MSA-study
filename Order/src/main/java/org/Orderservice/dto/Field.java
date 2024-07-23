package org.Orderservice.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Field {
    private String type;
    private boolean optional;
    private String field;



}
