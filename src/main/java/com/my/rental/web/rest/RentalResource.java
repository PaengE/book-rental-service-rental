package com.my.rental.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.rental.adaptor.BookClient;
import com.my.rental.adaptor.UserClient;
import com.my.rental.domain.Rental;
import com.my.rental.repository.RentalRepository;
import com.my.rental.service.RentalService;
import com.my.rental.web.rest.dto.BookInfoDTO;
import com.my.rental.web.rest.dto.LateFeeDTO;
import com.my.rental.web.rest.dto.RentalDTO;
import com.my.rental.web.rest.errors.BadRequestAlertException;
import com.my.rental.web.rest.errors.FeignClientException;
import com.my.rental.web.rest.errors.RentUnavailableException;
import com.my.rental.web.rest.mapper.RentalMapper;
import feign.FeignException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.my.rental.domain.Rental}.
 */
@RestController
@RequestMapping("/api")
public class RentalResource {

    private final Logger log = LoggerFactory.getLogger(RentalResource.class);

    private static final String ENTITY_NAME = "rentalRental";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final RentalRepository rentalRepository;
    private final BookClient bookClient;
    private final UserClient userClient;

    public RentalResource(
        RentalService rentalService,
        RentalMapper rentalMapper,
        RentalRepository rentalRepository,
        BookClient bookClient,
        UserClient userClient
    ) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.rentalRepository = rentalRepository;
        this.bookClient = bookClient;
        this.userClient = userClient;
    }

    /**
     * {@code POST  /rentals} : Create a new rental.
     *
     * @param rentalDTO the rentalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalDTO, or with status {@code 400 (Bad Request)} if the rental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rentals")
    public ResponseEntity<RentalDTO> createRental(@RequestBody RentalDTO rentalDTO) throws URISyntaxException {
        log.debug("REST request to save Rental : {}", rentalDTO);
        if (rentalDTO.getId() != null) {
            throw new BadRequestAlertException("A new rental cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalDTO result = rentalMapper.toDto(rentalService.save(rentalMapper.toEntity(rentalDTO)));

        return ResponseEntity
            .created(new URI("/api/rentals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rentals} : Updates an existing rental.
     *
     * @param rentalDTO the rentalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalDTO,
     * or with status {@code 400 (Bad Request)} if the rentalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rentals")
    public ResponseEntity<RentalDTO> updateRental(@RequestBody RentalDTO rentalDTO) throws URISyntaxException {
        log.debug("REST request to update Rental : {}", rentalDTO);
        if (rentalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RentalDTO result = rentalMapper.toDto(rentalService.save(rentalMapper.toEntity(rentalDTO)));
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rentals/:id} : Updates an existing rental.
     *
     * @param id        the id of the rentalDTO to save.
     * @param rentalDTO the rentalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalDTO,
     * or with status {@code 400 (Bad Request)} if the rentalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> updateRental(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RentalDTO rentalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Rental : {}, {}", id, rentalDTO);
        if (rentalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RentalDTO result = rentalMapper.toDto(rentalService.save(rentalMapper.toEntity(rentalDTO)));
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rentals} : get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentals in body.
     */
    @GetMapping("/rentals")
    public ResponseEntity<List<RentalDTO>> getAllRentals(Pageable pageable) {
        log.debug("REST request to get a page of Rentals");
        Page<RentalDTO> rentalPage = rentalService.findAll(pageable).map(rentalMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), rentalPage);
        return ResponseEntity.ok().headers(headers).body(rentalPage.getContent());
    }

    /**
     * {@code GET  /rentals/:id} : get the "id" rental.
     *
     * @param id the id of the rentalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        log.debug("REST request to get Rental : {}", id);
        RentalDTO rentalDTO = rentalMapper.toDto(rentalService.findOne(id).get());
        return ResponseEntity.ok().body(rentalDTO);
    }

    /**
     * {@code DELETE  /rentals/:id} : delete the "id" rental.
     *
     * @param id the id of the rentalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rentals/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        log.debug("REST request to delete Rental : {}", id);
        rentalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * ?????? ?????? ??????
     *
     * @param userid
     * @param bookId
     * @return
     */
    @PostMapping("/rentals/{userid}/RentedItem/{book}")
    public ResponseEntity<RentalDTO> rentBooks(@PathVariable("userid") Long userid, @PathVariable("book") Long bookId)
        throws InterruptedException, ExecutionException, JsonProcessingException, RentUnavailableException {
        log.debug("rent book request");

        ResponseEntity<BookInfoDTO> bookInfoResult = bookClient.findBookInfo(bookId); // feign - ??? ?????? ????????????
        BookInfoDTO bookInfoDTO = bookInfoResult.getBody();
        log.debug("book info list", bookInfoDTO.toString());

        Rental rental = rentalService.rentBook(userid, bookInfoDTO.getId(), bookInfoDTO.getTitle());
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        return ResponseEntity.ok().body(rentalDTO);
    }

    /**
     * ?????? ?????? ??????
     *
     * @param userid
     * @param book
     * @return
     */
    @DeleteMapping("/rentals/{userid}/RentedItem/{book}")
    public ResponseEntity<RentalDTO> returnBooks(@PathVariable("userid") Long userid, @PathVariable("book") Long book)
        throws InterruptedException, ExecutionException, JsonProcessingException {
        Rental rental = rentalService.returnBook(userid, book);
        log.debug("returned books");
        log.debug("SEND BOOKIDS for Book: {}", book);

        RentalDTO result = rentalMapper.toDto(rental);
        return ResponseEntity.ok().body(result);
    }

    /**
     * ?????? ???????????? ??????
     *
     * @param rentalId
     * @param bookId
     *
     */
    @PostMapping("/rentals/{rentalId}/OverdueItem/{bookId}")
    public ResponseEntity<Void> BeOverdue(@PathVariable("rentalId") Long rentalId, @PathVariable("bookId") Long bookId) {
        rentalService.beOverdueBook(rentalId, bookId);
        return ResponseEntity.ok().build();
    }

    /**
     * ???????????? ????????????
     *
     * @param userid
     * @param book
     * @return
     */
    @DeleteMapping("/rentals/{userid}/OverdueItem/{book}")
    public ResponseEntity returnOverdueBook(@PathVariable("userid") Long userid, @PathVariable("book") Long book)
        throws InterruptedException, ExecutionException, JsonProcessingException {
        Rental rental = rentalService.returnOverdueBook(userid, book);
        RentalDTO result = rentalMapper.toDto(rental);
        return ResponseEntity.ok().body(result);
    }

    /**
     * ???????????? ????????????
     *
     * @param userId
     * @return
     */
    @PutMapping("/rentals/release-overdue/user/{userId}")
    public ResponseEntity releaseOverdue(@PathVariable("userId") Long userId) {
        LateFeeDTO latefeeDTO = new LateFeeDTO();
        latefeeDTO.setUserId(userId);
        latefeeDTO.setLateFee(rentalService.findLateFee(userId));

        try {
            userClient.usePoint(latefeeDTO);
        } catch (FeignClientException e) {
            if (Integer.valueOf(HttpStatus.NOT_FOUND.value()).equals(e.getStatus())) {
                throw e;
            }
        }

        RentalDTO rentalDTO = rentalMapper.toDto(rentalService.releaseOverdue(userId));
        return ResponseEntity.ok().body(rentalDTO);
    }

    @GetMapping("/rentals/loadInfo/{userId}")
    public ResponseEntity loadRentalInfo(@PathVariable("userId") Long userId) {
        RentalDTO rentalDTO = rentalMapper.toDto(rentalService.findRentalByUser(userId).get());
        return ResponseEntity.ok().body(rentalDTO);
    }
}
