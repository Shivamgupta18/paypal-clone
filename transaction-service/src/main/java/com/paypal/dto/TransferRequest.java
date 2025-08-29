package com.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TransferRequest {

	private String senderName;
	private String receiverName;
	private Double amount;
	
}
