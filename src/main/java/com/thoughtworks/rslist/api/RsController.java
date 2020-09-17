package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.EventIndexException;
import com.thoughtworks.rslist.exception.EventRangeException;
import com.thoughtworks.rslist.exception.ExceptionMessage;
import com.thoughtworks.rslist.exception.HandleException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;

  @GetMapping("/rs/event/{index}")
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws EventIndexException {
    Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(index);
    if (!optionalRsEventEntity.isPresent()) {
      throw new EventIndexException();
    }
    RsEventEntity rsEventEntity = optionalRsEventEntity.get();
    return ResponseEntity.ok().body(new RsEvent(rsEventEntity.getEventName(),
            rsEventEntity.getKeyword(),
            rsEventEntity.getUserId()));
  }

  @GetMapping("/rs/event")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end) throws EventRangeException {
    List<RsEvent> rsList = rsEventRepository.findAll().stream()
            .map(rsEventEntity -> new RsEvent(rsEventEntity.getEventName(),
                    rsEventEntity.getKeyword(),
                    rsEventEntity.getUserId()))
            .collect(Collectors.toList());
    if (start == null || end == null) {
      return ResponseEntity.ok().body(rsList);
    }
    if (start < 1 || start > rsList.size() || end < start || end > rsList.size()) {
      throw new EventRangeException();
    }
    return ResponseEntity.ok().body(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity<Object> addOneRsEvent(@Valid @RequestBody RsEvent rsEvent) {

    if (!userRepository.existsById(rsEvent.getUserId())) {
      return ResponseEntity.status(400)
              .build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .userId(rsEvent.getUserId())
            .keyword(rsEvent.getKeyword())
            .build();
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.status(201)
            .header("index", String.valueOf(rsEventRepository.findAll().indexOf(rsEventEntity) + 1))
            .build();
  }

  @PutMapping("/rs/update/event/{id}")
  public ResponseEntity<Object> updateRsEventByIndex(@PathVariable Integer id, @RequestBody RsEvent rsEvent) {
    if (rsEvent == null || rsEvent.getUserId() == null) {
      return ResponseEntity.badRequest().build();
    }
    Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(id);
    if (!optionalRsEventEntity.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity rsEventEntity = optionalRsEventEntity.get();
    if (rsEventEntity.getUserId() != rsEvent.getUserId()) {
      return ResponseEntity.badRequest().build();
    }
    if (rsEvent.getKeyword() != null) {
      rsEventEntity.setKeyword(rsEvent.getKeyword());
    }
    if (rsEvent.getEventName() != null) {
      rsEventEntity.setEventName(rsEvent.getEventName());
    }
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/delete/event/{index}")
  public ResponseEntity<String> deleteRsEventByIndex(@PathVariable Integer index) {
    Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(index);
    if (!optionalRsEventEntity.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    rsEventRepository.deleteById(index);
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
    ExceptionMessage exceptionMessage = new ExceptionMessage();
    Logger logger = LoggerFactory.getLogger(RsController.class);
    if (ex instanceof MethodArgumentNotValidException) {
      exceptionMessage.setError("invalid param");
      logger.error(exceptionMessage.getError());
      return ResponseEntity.status(400).body(exceptionMessage);
    }
    logger.error(exceptionMessage.getError());
    return ResponseEntity.status(400).body(null);
  }
}
