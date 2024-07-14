package com.morning.mem.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("memService")
public class MemService {

	// 提供CRUD
	@Autowired
	MemRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public MemVO getOneMem(Integer memno) {
		Optional<MemVO> optional = repository.findById(memno);
		return optional.orElse(null);
	}

	public List<MemVO> getAll() {
		return repository.findAll();
	}

	public void addMem(MemVO memVO) {
		repository.save(memVO);
	}

	public void updateMem(MemVO memVO) {
		repository.save(memVO);
	}

	public void deleteMem(Integer memno) {
		if (repository.existsById(memno)) {
			repository.deleteByMemId(memno);
		}
	}

	public MemVO getMemberByEmail(String email) {
		return repository.findByMemEmail(email);
	}

	// 註冊邏輯
	public void registerMember(MemVO memVO) throws Exception {
		repository.save(memVO);
	}

	// 登入邏輯
	public boolean authenticateMember(MemVO memVO) {
		MemVO existingMember = repository.findByMemEmail(memVO.getMemEmail());
		if (existingMember == null) {
			return false;
		}
		return passwordEncoder.matches(memVO.getMemPassword(), existingMember.getMemPassword());
	}

	// 圖片=================================================================
	public byte[] getMemberImage(Integer memberId) {
		MemVO memVO = repository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
		return memVO.getUpFiles(); // 返回圖片數據
	}

	// 忘記密碼=======================================================================
	public boolean emailExists(String email) {
		return repository.findByMemEmail(email) != null;
	}

	public String getCurrentPassword(String email) {
		MemVO member = repository.findByMemEmail(email);
		return member != null ? member.getMemPassword() : null;
	}

	// 修改密碼=======================================================================
	public boolean updatePassword(String email, String newPassword) {
		MemVO member = repository.findByMemEmail(email);
		if (member != null) {
			member.setMemPassword(passwordEncoder.encode(newPassword));
			repository.save(member);
			return true;
		}
		return false;
	}

}
