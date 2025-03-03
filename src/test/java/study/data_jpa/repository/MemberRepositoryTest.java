package study.data_jpa.repository;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(savedMember.getId()).isEqualTo(findMember.getId());
    }

    @Test
    public void findByUsernameAndGraterThen() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));



        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);       // 페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);   // 전체 페이지 갯수
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void slice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();

        Assertions.assertThat(content.size()).isEqualTo(3);
//        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);       // 페이지 번호
//        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);   // 전체 페이지 갯수
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }
}