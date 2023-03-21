package com.example.shortchainssystem.vo;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortChainsRequest implements Serializable {

    @NotBlank
    private String sourceUrl;
}
