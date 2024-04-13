package com.learning.ecommerce.converters;

import com.learning.ecommerce.dto.ServiceabilityDto;
import com.learning.ecommerce.models.Serviceability;

import java.text.ParseException;

public class ServiceabilityDtoConverter {
	public ServiceabilityDto convertEntityToDto(Serviceability serviceability) {

		ServiceabilityDto serviceabilityDto = new ServiceabilityDto();

		serviceabilityDto.setPincode(serviceability.getPincode());

		serviceabilityDto.setExpectedDelivery(DateConverter.covertDaysIntoDate(serviceability.getExpectedDelivery()));

		return serviceabilityDto;
	}

	public Serviceability convertDtoToEntity(ServiceabilityDto serviceabilityDto) throws ParseException {
		Serviceability serviceability = new Serviceability();

		serviceability.setPincode(serviceabilityDto.getPincode());
		if(serviceabilityDto.getExpectedDelivery().length()<6){
			serviceability.setExpectedDelivery(Integer.parseInt(serviceabilityDto.getExpectedDelivery()));
		}
		else{
			serviceability.setExpectedDelivery(DateConverter.convertDateIntoDays(serviceabilityDto.getExpectedDelivery()));
		}

		return serviceability;
	}
}
