package com.example.mat.repository.shin;

import java.util.List;

public interface MemberImageRepository {

    List<Object[]> getMemberRow(Long mid);

}