import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.css',
})
export class SearchComponent implements OnInit {
  searchParam: String = '';
  searchForm = this.formBuilder.group({
    searchParam: [''],
  });
  isValid: boolean = true;
  isClicked: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private layoutService: LayoutService
  ) {}

  ngOnInit(): void {}

  onSearch(event: MouseEvent) {
    this.isClicked = true;
    event.preventDefault(); // Prevent form submission
    const searchParam = this.searchForm.value.searchParam;
    if (searchParam) {
      this.layoutService.search(searchParam).subscribe({
        next: (result) => {
          this.isValid = result;
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
  }
}
