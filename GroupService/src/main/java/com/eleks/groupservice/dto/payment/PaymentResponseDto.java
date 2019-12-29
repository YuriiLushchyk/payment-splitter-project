package com.eleks.groupservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class PaymentResponseDto extends PaymentRequestDto {

    private Long id;
    private Long creatorId;
    private Long groupId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss'T'dd-MM-yyyy")
    private LocalDateTime timestamp;
}
