package org.choongang.thesisAdvance.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.QField;
import org.choongang.thesis.entities.QThesisViewDaily;
import org.choongang.thesis.entities.QUserLog;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.services.WishListService;
import org.choongang.thesisAdvance.controllers.TrendSearch;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrendInfoService {

    private final JPAQueryFactory queryFactory;
    private final FieldRepository fieldRepository;
    public final WishListService wishListService;

    // 직업별 인기 키워드 조회
    public List<Map<String, Object>> getKeywordRankingByJob(TrendSearch search) {
        LocalDate sDate = Objects.requireNonNullElse(search.getSDate(), LocalDate.now().minusYears(1L));
        LocalDate eDate = Objects.requireNonNullElse(search.getEDate(), LocalDate.now());

        QUserLog userLog = QUserLog.userLog;


        List<Tuple> items = queryFactory.select(userLog.job, userLog.search.count(), userLog.search, userLog.searchDate)
                .from(userLog)
                .groupBy(userLog.job, userLog.search, userLog.searchDate)
                .orderBy(userLog.search.count().desc())
                .fetch();

        if (items != null && !items.isEmpty()) {
            return items.stream().filter(t -> t.get(userLog.searchDate).isAfter(sDate.atStartOfDay()) && t.get(userLog.searchDate).isBefore(eDate.atTime(LocalTime.MAX))).map(t -> {
                Map<String, Object> data = new HashMap<>();
                data.put("job", t.get(userLog.job));
                data.put("count", t.get(userLog.search.count()));
                data.put("search", t.get(userLog.search));
                return data;
            }).toList();
        }
        return Collections.EMPTY_LIST;
    }

    // 중분류 -> 조회된 만큼 카운트
    public Map<String, Map<String, Object>> getFieldRanking(TrendSearch search) {
        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();
        if (sDate == null) {
            return null;
        }

        QThesisViewDaily daily = QThesisViewDaily.thesisViewDaily;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(daily.date.goe(sDate));

        if (eDate != null) {
            builder.and(daily.date.loe(eDate));
        }

        List<Tuple> items = queryFactory.select(daily.tid, daily.fields)
                .from(daily)
                .where(builder)
                .fetch();

        if (items == null || items.isEmpty()) {
            return null;
        }

        List<String> fieldIds = items.stream().flatMap(s -> Arrays.stream(s.get(daily.fields).split(","))).distinct().toList();

        QField field = QField.field;
        List<Field> fields = (List<Field>) fieldRepository.findAll(field.id.in(fieldIds));
        Map<String, Map<String, Object>> statData = fields.stream().filter(f -> StringUtils.hasText(f.getName())).collect(Collectors.toMap(Field::getId, f -> {
            Map<String, Object> data = new HashMap<>();
            data.put("name", f.getName());
            data.put("subfield", f.getSubfield());
            data.put("count", 0);

            return data;
        }));

        for (Tuple item : items) {
            for (String name : item.get(daily.fields).split(",")) {
                Map<String, Object> data = statData.get(name);
                if (data == null) {
                    data = new HashMap<>();
                    data.put("count", 1);
                } else {
                    int count = (int) data.getOrDefault("count", 0);
                    data.put("count", count + 1);
                }
                statData.put(name, data); // 통계 데이터 쌓기
            }
        }

        /* 찜하기 데이터 처리 S */
        Map<String, Long> wishCounts = new HashMap<>();
        for (Tuple item : items) {
            Long tid = item.get(daily.tid);
            String _fields = item.get(daily.fields);
            long count = wishListService.getCount(tid);
            for (String _field : _fields.split(",")) {
                long _count = wishCounts.getOrDefault(_field, 0L);
                wishCounts.put(_field, _count + count);
            }
        }

        //statData 에 찜하기 데이터 추가 처리
        for (Map.Entry<String, Long> entry : wishCounts.entrySet()) {
            String key = entry.getKey();
            long count = entry.getValue();
            Map<String, Object> data = statData.get(key);
            data.put("wishCount", count);
        }
        /* 찜하기 데이터 처리 E */

        return statData;
    }

}

