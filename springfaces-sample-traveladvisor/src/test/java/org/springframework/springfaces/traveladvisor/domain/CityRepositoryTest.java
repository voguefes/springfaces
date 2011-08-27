package org.springframework.springfaces.traveladvisor.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/config/data-access-config.xml")
public class CityRepositoryTest {

	@Autowired
	private CityRepository cityRepository;

	private PageRequest pageable = new PageRequest(0, 10);

	@Test
	public void shouldFindNotResultsByName() throws Exception {
		Page<City> page = cityRepository.findByNameAndCountryLikeAllIgnoringCase("notfound%", "%%", pageable);
		assertThat(page.getTotalElements(), is(0L));
	}

	@Test
	public void shouldFindSingleResultByName() throws Exception {
		Page<City> page = cityRepository.findByNameAndCountryLikeAllIgnoringCase("Bath%", "%%", pageable);
		assertThat(page.getTotalElements(), is(1L));
		assertThat(page.getContent().get(0).getName(), is("Bath"));
	}

	@Test
	public void shouldFindByNameIgnoringCase() throws Exception {
		Page<City> page = cityRepository.findByNameAndCountryLikeAllIgnoringCase("bAtH%", "%%", pageable);
		assertThat(page.getTotalElements(), is(1L));
		assertThat(page.getContent().get(0).getName(), is("Bath"));
	}

	@Test
	public void shouldFindMoreThanOneCity() throws Exception {
		Page<City> page = cityRepository.findByNameAndCountryLikeAllIgnoringCase("Melbourne%", "%%", pageable);
		assertThat(page.getTotalElements(), is(2L));
	}

	@Test
	public void shouldFindByNameAndCountryLike() throws Exception {
		Page<City> page = cityRepository.findByNameAndCountryLikeAllIgnoringCase("Melbourne%", "%Aus%", pageable);
		assertThat(page.getTotalElements(), is(1L));
	}

	@Test
	public void shouldFindByNameAndCountry() throws Exception {
		City melbourneUsa = cityRepository.findByNameAndCountry("Melbourne", "USA");
		City melbourneAustralia = cityRepository.findByNameAndCountry("Melbourne", "Australia");
		assertThat(melbourneUsa.getName(), is("Melbourne"));
		assertThat(melbourneUsa.getCountry(), is("USA"));
		assertThat(melbourneAustralia.getName(), is("Melbourne"));
		assertThat(melbourneAustralia.getCountry(), is("Australia"));
	}

	@Test
	public void shouldReturnNullIfNotFoundByNameAndCountry() throws Exception {
		City city = cityRepository.findByNameAndCountry("Melbourne", "UK");
		assertThat(city, is(nullValue()));
	}
}
