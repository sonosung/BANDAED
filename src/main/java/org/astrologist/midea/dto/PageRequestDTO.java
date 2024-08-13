package org.astrologist.midea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    //멤버변수 선언.
    private int page;
    private int size;
    private String type;
    private String keyword;

    //기본생성자
    public PageRequestDTO(){
        //한 페이지에 출력되는 레코드 사이즈를 지정.
        this.page = 1;
        this.size = 5;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }
}
