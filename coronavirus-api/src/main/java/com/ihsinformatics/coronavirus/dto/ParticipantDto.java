/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.PersonService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
public class ParticipantDto {

    private Integer participantId;

    private String locationName;

    private String uuid;

    public ParticipantDto(Integer participantId, String uuid, String locationName) {
	this.participantId = participantId;
	this.locationName = locationName;
	this.uuid = uuid;
    }

    public Participant toParticipant(LocationService locationService, PersonService personService) {
	Location location = locationService.getLocationByUuid(uuid);
	Participant participant = Participant.builder().location(location).build();
	participant.setPerson(personService.getPersonByUuid(uuid));
	return participant;
    }
}
