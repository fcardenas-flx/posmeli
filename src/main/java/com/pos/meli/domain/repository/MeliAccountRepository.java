package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.MeliAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeliAccountRepository <T extends MeliAccount> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{
	MeliAccount findByNicknameAndSiteIdAndSellerId(String nickname, Long siteId, String sellerId);

	MeliAccount findByNickname(String nickname);
}
