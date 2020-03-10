/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.dto.UserDto;
import com.ihsinformatics.coronavirus.model.User;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class UserDtoTest extends BaseServiceTest {

    private UserDto userDto;

    @Before
    public void reset() {
	userDto = new UserDto(1, "nymphadora.tonks", "Nymphadora Tonks", UUID.randomUUID().toString(),
		new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void shouldConvertFromUser() {
	initUserAttributeTypes();
	initUserAttributes();
	tonks = User.builder().userId(1).username("nymphadora.tonks").fullName("Nymphadora Tonks")
		.attributes(Arrays.asList(tonksBlood, tonksPatronus)).build();
	tonks.setPassword("Stupify");
	assertNotNull(new UserDto(tonks));
    }

    @Test
    public void shouldConvertToUser() {
	assertNotNull(userDto.toUser(userService));
    }
}
