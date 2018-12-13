package com.application.dao;

import com.application.status.Status;

public interface Iauthority {
	Status getAuthority(int pid,String membername);
}